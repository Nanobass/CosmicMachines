package net.paxyinc.machines.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
import finalforeach.cosmicreach.settings.GraphicsSettings;
import finalforeach.cosmicreach.ui.*;
import finalforeach.cosmicreach.world.*;
import net.paxyinc.machines.entities.BetterEntity;
import net.paxyinc.machines.entities.IRenderableEntity;
import net.paxyinc.machines.entities.ItemEntity;
import net.paxyinc.machines.interfaces.WorldInterface;
import net.paxyinc.machines.io.AdvancedEntitySaveSystem;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import net.paxyinc.machines.ui.UI2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private static final UUID LOCAL_PLAYER_UUID = UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454");

    @Overwrite
    public void loadWorld(World world) {
        InGame.world = world;
        AdvancedEntitySaveSystem.loadEntities(world);
        // join as local player TODO change this
        WorldInterface wi = (WorldInterface) InGame.world;
        Player localPlayer = wi.loadPlayer(LOCAL_PLAYER_UUID);
        setLocalPlayer(localPlayer);
    }

    @Overwrite
    public void unloadWorld() {
        // kick locale player TODO change this
        WorldInterface wi = (WorldInterface) InGame.world;
        wi.unloadPlayer(LOCAL_PLAYER_UUID);
        setLocalPlayer(null);

        // wait for world loader
        synchronized(WorldLoader.worldGenLock) {
            WorldLoader.worldLoader.readyToPlay = false;
            // unload world
            if(InGame.world != null) {
                for (Zone z : InGame.world.getZones()) {
                    for (Region r : z.regions.values()) {
                        z.removeRegion(r);
                    }
                    z.dispose();
                }
            }
            InGame.world = null;
        }

        // unload the renderer
        GameSingletons.zoneRenderer.unload();
    }

    @Overwrite
    public void setWorld(World world) {
        System.err.println("Using setWorld is REALLY deprecated");
        unloadWorld();
        loadWorld(world);
    }


    @Overwrite
    public static void setLocalPlayer(Player player) {
        System.err.println("Using setLocalPlayer is deprecated");
        InGameMixin.player = player;
        if(InGameMixin.player != null) InGameMixin.player.updateCamera(rawWorldCamera, 0.0F);
    }


    @Overwrite
    public void create() {
        super.create();
        Gdx.graphics.setVSync(GraphicsSettings.vSyncEnabled.getValue());
        this.blockSelection = new BlockSelection();
        rawWorldCamera = new PerspectiveCamera(GraphicsSettings.fieldOfView.getValue(), (float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight());
        rawWorldCamera.near = 0.1F;
        rawWorldCamera.far = 2500.0F;
        this.viewport = new ExtendViewport(1.0F, 1.0F, rawWorldCamera);
        this.ui = new UI2();
        Gdx.input.setInputProcessor(this.ui);
    }

    @Overwrite
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (WorldLoader.worldLoader.readyToPlay) {
            rawWorldCamera.fieldOfView = GraphicsSettings.fieldOfView.getValue();
            if (player.getEntity() != null && player.getEntity().position.y < player.getZone(world).respawnHeight) {
                player.respawn(world);
            }

            for (Zone z : world.getZones()) {
                z.runScheduledTriggers();
                for (Entity e : z.allEntities) {
                    e.update(z, deltaTime);
                }
            }

        }
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

        List<BetterEntity> despawn = new ArrayList<>();

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

        for(BetterEntity entity : despawn) {
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
            if(entity instanceof IRenderableEntity renderable) {
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
