package net.paxyinc.entities;

import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Chunk;
import net.paxyinc.interfaces.ZoneInterface;
import net.paxyinc.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public class FunctionalBlock implements NbtSerializable, IFunctionalBlockCapabilities {

    public BlockPosition position;
    public Block block;
    public BlockState blockState;
    protected Map<Direction, FunctionalBlock> neighbors = new HashMap<>();

    public void onCreate() {
        ZoneInterface zi = (ZoneInterface) position.chunk.region.zone;
        neighbors = zi.forEachNeighbor(position, FunctionalBlock.class);
    }

    public void onInteract(Player player) {

    }

    public void onNeighborPlaced(Direction face, FunctionalBlock block) {
        neighbors.put(face, block);
    }

    public void onNeighborBroken(Direction face, FunctionalBlock block) {
        neighbors.remove(face, block);
    }

    public void onTick() {

    }

    public void onDestroy() {

    }

    @Override
    public void read(CompoundTag nbt) {

    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putInt("x", position.getGlobalX());
        nbt.putInt("y", position.getGlobalY());
        nbt.putInt("z", position.getGlobalZ());
    }

    public void initialize(CompoundTag nbt, Chunk chunk) {
        int globalX = nbt.getInt("x");
        int globalY = nbt.getInt("y");
        int globalZ = nbt.getInt("z");
        position = new BlockPosition(chunk, globalX - chunk.blockX, globalY - chunk.blockY, globalZ - chunk.blockZ);
        blockState = position.getBlockState();
        block = blockState.getBlock();
    }

    @Override
    public boolean canAcceptItems(Direction face) {
        return false;
    }

    @Override
    public boolean canOutputItems(Direction face) {
        return false;
    }

    @Override
    public boolean canAcceptEnergy(Direction face) {
        return false;
    }

    @Override
    public boolean canOutputEnergy(Direction face) {
        return false;
    }

    @Override
    public boolean canAcceptFluids(Direction face) {
        return false;
    }

    @Override
    public boolean canOutputFluids(Direction face) {
        return false;
    }

}
