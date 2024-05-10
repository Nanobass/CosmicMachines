package net.paxyinc.machines.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.gamestates.PauseMenu;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.*;
import finalforeach.cosmicreach.world.BlockSelection;
import finalforeach.cosmicreach.world.Sky;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.ItemEntity;
import net.paxyinc.machines.entities.RenderableEntity;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import net.paxyinc.machines.ui.UI2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = InGame.class, priority = 500)
public abstract class InGameMixin extends GameState {

    @Shadow public static World world;
    @Shadow private static Player player;
    @Shadow private static PerspectiveCamera rawWorldCamera;
    @Shadow private BlockSelection blockSelection;
    @Shadow private Viewport viewport;
    @Shadow private UI ui;
    @Shadow private transient float screenshotMsgCountdownTimer = 0.0F;
    @Shadow private transient String lastScreenshotFileName;

    @Inject(method = "create", at = @At("TAIL"))
    private void create(CallbackInfo ci) {
        ui = new UI2();
        Gdx.input.setInputProcessor(ui);
    }

    @Overwrite
    public void render(float partTick) {
        super.render(partTick);
        Zone playerZone = player.getZone(world);
        if (GameState.currentGameState == GameState.IN_GAME) {
            player.update(playerZone);
            this.blockSelection.raycast(playerZone, rawWorldCamera);
            player.updateCamera(rawWorldCamera, partTick);
        }

        List<RenderableEntity> despawn = new ArrayList<>();

        for(Entity entity : playerZone.allEntities) {
            if(entity instanceof ItemEntity itemEntity) {
                Vector3 diff = new Vector3(entity.position);
                diff.sub(player.getEntity().position);
                if(diff.len() < 1 && itemEntity.timer <= 0) {
                    PlayerInventory.inventory.put(itemEntity.items.item, itemEntity.items.amount);
                    despawn.add(itemEntity);
                }
            }
        }

        for(RenderableEntity entity : despawn) {
            entity.despawn(playerZone);
        }

        if (!this.firstFrame && Gdx.input.isKeyJustPressed(111)) {
            if (!UI2.closeAnyOpenInventories()) {
                boolean cursorCatched = Gdx.input.isCursorCatched();
                Gdx.input.setCursorCatched(false);
                switchToGameState(new PauseMenu(cursorCatched));
            }
        }

        ScreenUtils.clear(Sky.skyColor, true);
        this.viewport.apply();
        Sky.drawStars(rawWorldCamera);
        GameSingletons.zoneRenderer.render(playerZone, rawWorldCamera);
        this.blockSelection.render(rawWorldCamera);
        for(Entity entity : playerZone.allEntities) {
            if(entity instanceof RenderableEntity renderable) {
                renderable.render(rawWorldCamera);
            }
        }
        this.ui.render();
        this.drawUIElements();
        float maxScreenshotMsgCountdownTimer = 5.0F;
        if (Controls.screenshotPressed()) {
            this.lastScreenshotFileName = this.takeScreenshot();
            this.screenshotMsgCountdownTimer = maxScreenshotMsgCountdownTimer;
        }

        if (this.screenshotMsgCountdownTimer > 0.0F) {
            batch.setColor(1.0F, 1.0F, 1.0F, MathUtils.clamp(this.screenshotMsgCountdownTimer / maxScreenshotMsgCountdownTimer, 0.0F, 1.0F));
            batch.begin();
            String saveText;
            if (this.lastScreenshotFileName != null) {
                String var10000 = Lang.get("Screenshot_info");
                saveText = var10000 + this.lastScreenshotFileName.replace(SaveLocation.getSaveFolderLocation(), "");
            } else {
                saveText = Lang.get("Screenshot_error");
            }

            FontRenderer.drawText(batch, this.uiViewport, saveText, 0.0F, 8.0F, HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.TOP_ALIGNED);
            batch.end();
            this.screenshotMsgCountdownTimer -= Gdx.graphics.getDeltaTime();
            if (this.screenshotMsgCountdownTimer <= 0.0F) {
                this.lastScreenshotFileName = null;
            }

            batch.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

    }

}
