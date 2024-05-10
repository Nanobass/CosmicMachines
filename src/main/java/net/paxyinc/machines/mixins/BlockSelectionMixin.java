package net.paxyinc.machines.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blockevents.BlockEventTrigger;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.world.BlockSelection;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.ItemEntity;
import net.paxyinc.machines.entities.RenderableEntity;
import net.paxyinc.machines.entities.TileEntityManager;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemRegistry;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.item.ItemStack;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import net.paxyinc.machines.item.items.BlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockSelection.class)
public abstract class BlockSelectionMixin {

    @Shadow
    public static ShapeRenderer shapeRenderer = new ShapeRenderer();
    @Shadow public static boolean enabled;
    @Shadow private BlockState lastSelectedBlockState;
    @Shadow private static BlockState selectedBlockState;
    @Shadow private BlockPosition lastSelectedBlockPos;
    @Shadow private static BlockPosition selectedBlockPos;
    @Shadow private Array<BoundingBox> blockBoundingBoxes = new Array(4);
    @Shadow private Ray ray = new Ray();
    @Shadow private BoundingBox tmpBoundingBox = new BoundingBox();
    @Shadow private Vector3 intersection = new Vector3();
    @Shadow private float maximumRaycastDist = 6.0F;
    @Shadow private Array<BlockPosition> toVisit = new Array();
    @Shadow private Vector3 workingPos = new Vector3();
    @Shadow private Queue<BlockPosition> blockQueue = new Queue();
    @Shadow private double timeSinceBlockModify = 0.0;
    @Shadow private Vector3 mouseCoords = new Vector3();
    @Shadow private Vector3 mouseCoords2 = new Vector3();
    @Shadow private Array<BoundingBox> tmpBoundingBoxes = new Array();

    @Overwrite
    public void raycast(Zone zone, Camera worldCamera) {
        enabled = false;
        if (!UI.mouseOverUI) {
            BlockPosition placingBlockPos = null;
            BlockPosition breakingBlockPos = null;
            BlockPosition lastBlockPosAtPoint = null;
            BlockPosition lastBlockPosInQueue = null;
            this.toVisit.clear();
            this.blockQueue.clear();
            if (Gdx.input.isCursorCatched()) {
                this.ray.set(worldCamera.position, worldCamera.direction);
            } else {
                this.mouseCoords.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0.0F);
                this.mouseCoords2.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 1.0F);
                worldCamera.unproject(this.mouseCoords);
                worldCamera.unproject(this.mouseCoords2);
                this.mouseCoords2.sub(this.mouseCoords).nor();
                this.ray.set(this.mouseCoords, this.mouseCoords2);
            }

            this.workingPos.set(this.ray.origin);

            for(; this.workingPos.dst(this.ray.origin) <= this.maximumRaycastDist; this.workingPos.add(this.ray.direction)) {
                int bx = (int)Math.floor((double)this.workingPos.x);
                int by = (int)Math.floor((double)this.workingPos.y);
                int bz = (int)Math.floor((double)this.workingPos.z);
                int dx = 0;
                int dy = 0;
                int dz = 0;
                if (lastBlockPosAtPoint != null) {
                    if (lastBlockPosAtPoint.getGlobalX() == bx && lastBlockPosAtPoint.getGlobalY() == by && lastBlockPosAtPoint.getGlobalZ() == bz) {
                        continue;
                    }

                    dx = bx - lastBlockPosAtPoint.getGlobalX();
                    dy = by - lastBlockPosAtPoint.getGlobalY();
                    dz = bz - lastBlockPosAtPoint.getGlobalZ();
                }

                Chunk c = zone.getChunkAtBlock(bx, by, bz);
                if (c != null) {
                    BlockPosition nextBlockPos = new BlockPosition(c, bx - c.blockX, by - c.blockY, bz - c.blockZ);
                    if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) > 1) {
                        if (dx != 0) {
                            this.addBlockToQueue(zone, lastBlockPosAtPoint, dx, 0, 0);
                        }

                        if (dy != 0) {
                            this.addBlockToQueue(zone, lastBlockPosAtPoint, 0, dy, 0);
                        }

                        if (dz != 0) {
                            this.addBlockToQueue(zone, lastBlockPosAtPoint, 0, 0, dz);
                        }

                        if (dx != 0 && dy != 0) {
                            this.addBlockToQueue(zone, lastBlockPosAtPoint, dx, dy, 0);
                        }

                        if (dx != 0 && dz != 0) {
                            this.addBlockToQueue(zone, lastBlockPosAtPoint, dx, 0, dz);
                        }

                        if (dy != 0 && dz != 0) {
                            this.addBlockToQueue(zone, lastBlockPosAtPoint, 0, dy, dz);
                        }
                    }

                    if (nextBlockPos != null && !this.toVisit.contains(nextBlockPos, false)) {
                        BlockState block = nextBlockPos.getBlockState();
                        block.getBoundingBox(this.tmpBoundingBox, nextBlockPos);
                        if (Intersector.intersectRayBounds(this.ray, this.tmpBoundingBox, this.intersection)) {
                            this.blockQueue.addLast(nextBlockPos);
                            this.toVisit.add(nextBlockPos);
                        } else if (block.canRaycastForReplace()) {
                            this.tmpBoundingBox.min.set((float)nextBlockPos.getGlobalX(), (float)nextBlockPos.getGlobalY(), (float)nextBlockPos.getGlobalZ());
                            this.tmpBoundingBox.max.set(this.tmpBoundingBox.min).add(1.0F, 1.0F, 1.0F);
                            if (Intersector.intersectRayBounds(this.ray, this.tmpBoundingBox, this.intersection)) {
                                this.blockQueue.addLast(nextBlockPos);
                                this.toVisit.add(nextBlockPos);
                            }
                        }
                    }

                    label184:
                    while(true) {
                        BlockState blockState;
                        BlockPosition curBlockPos;
                        do {
                            if (!this.blockQueue.notEmpty()) {
                                break label184;
                            }

                            curBlockPos = (BlockPosition)this.blockQueue.removeFirst();
                            blockState = curBlockPos.getBlockState();
                        } while(!blockState.hasEmptyModel() && !this.intersectsWithBlock(blockState, curBlockPos));

                        if (breakingBlockPos == null && blockState.canRaycastForBreak()) {
                            breakingBlockPos = curBlockPos;
                            enabled = true;
                            selectedBlockState = blockState;
                            selectedBlockPos = curBlockPos;
                        }

                        if (placingBlockPos == null && blockState.canRaycastForPlaceOn() && lastBlockPosInQueue != null) {
                            BlockState lastBlockStateInQueue = lastBlockPosInQueue.getBlockState();
                            if (lastBlockStateInQueue.canRaycastForReplace()) {
                                placingBlockPos = lastBlockPosInQueue;
                                enabled = true;
                                selectedBlockState = blockState;
                                selectedBlockPos = curBlockPos;
                            }
                        }

                        if (breakingBlockPos != null && placingBlockPos != null) {
                            break;
                        }

                        lastBlockPosInQueue = curBlockPos;
                    }

                    if (breakingBlockPos != null && placingBlockPos != null) {
                        break;
                    }

                    lastBlockPosAtPoint = nextBlockPos;
                }
            }

            BlockState targetBlockState = null;
            // TODO get selected block here
            ItemSlot slot = PlayerInventory.inventory.getSelectedItem();
            if(slot.itemStack != null && slot.itemStack.item instanceof BlockItem blockItem) {
                targetBlockState = blockItem.block.getDefaultBlockState();
            }

            this.timeSinceBlockModify -= (double)Gdx.graphics.getDeltaTime();
            boolean breakPressed = this.timeSinceBlockModify <= 0.0 && Controls.breakPressed();
            boolean placePressed = this.timeSinceBlockModify <= 0.0 && Controls.placePressed();
            breakPressed |= Gdx.input.isButtonJustPressed(0);
            placePressed |= Gdx.input.isButtonJustPressed(1);
            boolean interactJustPressed = Gdx.input.isButtonJustPressed(1);
            if (breakingBlockPos != null && Controls.pickBlockPressed()) {
                // TODO pick block here
                Identifier blockId = Identifier.fromString(breakingBlockPos.getBlockState().getBlockId());
                Item item = ItemRegistry.allItems.access().get(blockId);
                PlayerInventory.inventory.pickBlock(item);
            }

            if (breakingBlockPos != null && breakPressed) {
                Identifier blockId = Identifier.fromString(breakingBlockPos.getBlockState().getBlockId());
                this.breakBlock(zone, breakingBlockPos, this.timeSinceBlockModify);
                Item item = ItemRegistry.allItems.access().get(blockId);
                // TODO spawning item here
                ItemEntity entity = new ItemEntity(item, 1);
                RenderableEntity.spawn(zone, breakingBlockPos.getGlobalX() + 0.5f, breakingBlockPos.getGlobalY() + 0.5f, breakingBlockPos.getGlobalZ() + 0.5f, entity);
                entity.velocity.add(MathUtils.random(-2, 2), MathUtils.random(0, 1), MathUtils.random(-2, 2));
                this.timeSinceBlockModify = 0.25;
            }

            if (placingBlockPos != null && placePressed && targetBlockState != null) {
                Entity playerEntity = InGame.getLocalPlayer().getEntity();
                boolean positionBlockedByPlayer = false;
                if (!targetBlockState.walkThrough) {
                    BoundingBox blockBoundingBox = new BoundingBox();
                    BoundingBox playerBoundingBox = new BoundingBox();
                    playerBoundingBox.set(playerEntity.localBoundingBox);
                    playerBoundingBox.min.add(playerEntity.position);
                    playerBoundingBox.max.add(playerEntity.position);
                    playerBoundingBox.update();
                    targetBlockState.getBoundingBox(blockBoundingBox, placingBlockPos);
                    if (blockBoundingBox.intersects(playerBoundingBox) && blockBoundingBox.max.y - playerBoundingBox.min.y > playerEntity.maxStepHeight) {
                        positionBlockedByPlayer = true;
                    }
                }

                if (!positionBlockedByPlayer || playerEntity.noClip) {
                    this.timeSinceBlockModify = 0.25;
                    // TODO place block here
                    if(slot.take(1)) {
                        this.placeBlock(zone, targetBlockState, placingBlockPos, this.timeSinceBlockModify);
                    }
                }
            } else if (breakingBlockPos != null && (interactJustPressed || placePressed)) {
                this.interactWith(zone, breakingBlockPos, interactJustPressed, placePressed, this.timeSinceBlockModify);
                TileEntityManager.MANAGER.onBlockInteract(zone, breakingBlockPos, InGame.getLocalPlayer(), timeSinceBlockModify);
                this.timeSinceBlockModify = 0.25;
            }

        }
    }

    @Shadow protected abstract boolean intersectsWithBlock(BlockState block, BlockPosition nextBlockPos);

    @Shadow protected abstract void addBlockToQueue(Zone zone, BlockPosition bp, int dx, int dy, int dz);

    @Shadow protected abstract void breakBlock(Zone zone, BlockPosition blockPos, double timeSinceLastInteract);

    @Shadow protected abstract void placeBlock(Zone zone, BlockState targetBlockState, BlockPosition blockPos, double timeSinceLastInteract);

    @Shadow protected abstract void interactWith(Zone zone, BlockPosition blockPos, boolean interactJustPressed, boolean interactHeld, double timeSinceLastInteract);

}
