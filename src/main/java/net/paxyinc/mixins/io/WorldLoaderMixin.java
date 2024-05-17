package net.paxyinc.mixins.io;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.io.ChunkLoader;
import finalforeach.cosmicreach.io.ChunkSaver;
import finalforeach.cosmicreach.settings.GraphicsSettings;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.WorldLoader;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;
import finalforeach.cosmicreach.worldgen.ChunkColumnCoords;
import net.paxyinc.io.FunctionalBlockSaveSystem;
import net.paxyinc.io.NbtEntitySaveSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mixin(WorldLoader.class)
public class WorldLoaderMixin {

    @Shadow HashMap<ChunkColumnCoords, ChunkColumn> loadedChunkColumns;
    @Shadow Array<ChunkColumnCoords> chunkColumnsToRemove;
    @Shadow public float loadProgress;

    @Overwrite
    public ChunkColumn getChunkColumn(Zone zone, int chunkX, int chunkZ, boolean createIfNoneFound) {
        ChunkColumnCoords colCoords = new ChunkColumnCoords(chunkX, chunkZ);
        ChunkColumn cc = this.loadedChunkColumns.get(colCoords);
        if (cc == null && createIfNoneFound) {
            cc = new ChunkColumn(chunkX, chunkZ);
            ChunkLoader.readChunkColumn(zone, cc);
            Array<Chunk> chunks = new Array<>(Chunk.class);
            cc.getChunks(zone, chunks);

            try {
                FunctionalBlockSaveSystem.Instance.loadChunks(chunks);
                NbtEntitySaveSystem.Instance.loadChunks(chunks);
            } catch (IOException e) {
                e.printStackTrace();
            }

            chunks.forEach((c) -> {
                c.flagHorizontalTouchingChunksForRemeshing(zone, false);
            });
            this.loadedChunkColumns.put(colCoords, cc);

            int localRenderRadiusInChunks = GraphicsSettings.renderDistanceInChunks.getValue();
            int lesserRadius = MathUtils.clamp(localRenderRadiusInChunks, 6, 12);
            loadProgress = (float) loadedChunkColumns.size() / (lesserRadius * lesserRadius * 3.1415f);

        }
        return cc;
    }

    @Overwrite
    private void unloadFarAwayChunks(Zone zone, int chunkRadius, int playerChunkX, int playerChunkZ, Array<Chunk> tmpColChunks) {
        chunkColumnsToRemove.clear();
        Iterator<Map.Entry<ChunkColumnCoords, ChunkColumn>> it = loadedChunkColumns.entrySet().iterator();

        while(true) {
            Map.Entry<ChunkColumnCoords, ChunkColumn> c;
            ChunkColumn cc;
            float chunkDistSq;
            do {
                if (!it.hasNext()) {
                    chunkColumnsToRemove.forEach((coords) -> {
                        loadedChunkColumns.remove(coords);
                    });

                    try {
                        FunctionalBlockSaveSystem.Instance.clearCache();
                        NbtEntitySaveSystem.Instance.clearCache();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return;
                }
                c = it.next();
                cc = c.getValue();
                chunkDistSq = Vector2.dst2((float)cc.chunkX, (float)cc.chunkZ, (float)playerChunkX, (float)playerChunkZ);
            } while(!(chunkDistSq > (float)(1 + chunkRadius * chunkRadius)));

            Array<Chunk> colChunks = cc.getChunks(zone, tmpColChunks);

            try {
                FunctionalBlockSaveSystem.Instance.saveChunks(colChunks);
                NbtEntitySaveSystem.Instance.saveChunks(colChunks);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Chunk chunk;
            for(Array.ArrayIterator<Chunk> cit = colChunks.iterator(); cit.hasNext(); zone.removeChunk(chunk)) {
                chunk = cit.next();
                if (!chunk.isSaved) {
                    ChunkSaver.saveRegion(zone, chunk.region);
                }
            }

            this.chunkColumnsToRemove.add(c.getKey());
        }

    }

}
