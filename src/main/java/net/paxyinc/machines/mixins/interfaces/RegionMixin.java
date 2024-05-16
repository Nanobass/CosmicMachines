package net.paxyinc.machines.mixins.interfaces;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.RegionOctant;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.interfaces.RegionInterface;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Region.class)
public class RegionMixin implements RegionInterface {
    
    @Shadow private Array<Chunk> chunks;
    @Shadow public @Final Zone zone;
    @Shadow public @Final BoundingBox boundingBox;
    @Shadow public @Final RegionOctant octant_nxnynz;
    @Shadow public @Final RegionOctant octant_nxnypz;
    @Shadow public @Final RegionOctant octant_nxpynz;
    @Shadow public @Final RegionOctant octant_nxpypz;
    @Shadow public @Final RegionOctant octant_pxnynz;
    @Shadow public @Final RegionOctant octant_pxnypz;
    @Shadow public @Final RegionOctant octant_pxpynz;
    @Shadow public @Final RegionOctant octant_pxpypz;
    @Shadow public @Final RegionOctant[] octants;
    @Shadow public @Final int regionX;
    @Shadow public @Final int regionY;
    @Shadow public @Final int regionZ;
    @Shadow public @Final int blockX;
    @Shadow public @Final int blockY;
    @Shadow public @Final int blockZ;
    @Shadow public int[] fileChunkByteOffsets;
    @Shadow public transient boolean[][] columnsGenerated;
    @Shadow public boolean flaggedForRemeshing;


}
