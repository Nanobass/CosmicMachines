package net.paxyinc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.insertsoda.craterchat.CraterChat;
import dev.crmodders.flux.ui.Component;
import dev.crmodders.flux.ui.UIRenderer;
import dev.crmodders.flux.ui.text.TextBatch;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.gamestates.PauseMenu;
import finalforeach.cosmicreach.io.ChunkSaver;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.*;
import finalforeach.cosmicreach.ui.debug.DebugInfo;
import net.paxyinc.interfaces.PlayerInterface;
import net.paxyinc.item.IEntityInventory;
import net.paxyinc.item.IItemInventoryRenderer;
import net.paxyinc.item.ItemInventory;
import net.paxyinc.item.inventories.MouseInventory;
import net.paxyinc.item.inventories.PlayerInventory;
import net.paxyinc.item.renderers.MouseInventoryRenderer;
import net.paxyinc.item.renderers.player.InGameHotbarRenderer;
import net.paxyinc.item.renderers.player.PlayerInventoryRenderer;
import net.paxyinc.item.renderers.player.SubPlayerInventoryRenderer;
import net.paxyinc.mixins.InGameMixin;
import net.paxyinc.mixins.io.ChunkSaverMixin;
import net.paxyinc.ui.elements.BaseItemElement;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class UI2 extends UI {

    public Viewport uiViewport;
    public OrthographicCamera uiCamera;
    public Crosshair crosshair = new Crosshair();

    public static List<InGameUI> alwaysActiveUIs = new ArrayList<>();
    public static List<InGameUI> inGameOnlyUIs = new ArrayList<>();
    public static List<InGameUI> activeInGameUIs = new ArrayList<>();
    public static InGameUI activeBlockUI = null;

    public static MouseInventoryRenderer mouseInventoryRenderer;

    public static PlayerInventory playerInventory;
    public static InGameHotbarRenderer playerHotbarRenderer = null;

    public boolean renderDebugInfo = false;

    static {
        alwaysActiveUIs.add(mouseInventoryRenderer = new MouseInventoryRenderer(MouseInventory.mouseInventory));
    }

    public static void openBlockUI(InGameUI ui) {
        if(activeBlockUI != null) {
            activeInGameUIs.remove(activeBlockUI);
        }
        activeBlockUI = ui;
        activeInGameUIs.add(ui);
    }

    public static void openPlayerInventory(boolean sub) {
        if(sub) {
            SubPlayerInventoryRenderer renderer = new SubPlayerInventoryRenderer(playerInventory);
            activeInGameUIs.add(renderer);
        } else {
            PlayerInventoryRenderer renderer = new PlayerInventoryRenderer(playerInventory);
            activeInGameUIs.add(renderer);
        }
    }

    public static void closeBlockUI() {
        if(activeBlockUI != null) {
            activeInGameUIs.remove(activeBlockUI);
            activeBlockUI = null;
        }
    }

    public static boolean areAnyInventoriesOpen() {
        return activeInGameUIs.stream().anyMatch(ui -> ui instanceof IItemInventoryRenderer);
    }

    public static boolean closeAnyOpenInventories() {
        boolean wereAnyInventoriesOpen = areAnyInventoriesOpen();
        if(activeBlockUI != null) closeBlockUI();
        activeInGameUIs.clear();
        MouseInventory.mouseInventory.dropItems(InGame.getLocalPlayer());
        return wereAnyInventoriesOpen;
    }

    public static void createLocalPlayerUI(Player player) {
        if(playerHotbarRenderer != null) {
            inGameOnlyUIs.remove(playerHotbarRenderer);
        }

        PlayerInterface pi = (PlayerInterface) player;
        playerInventory = pi.getInventory();
        playerHotbarRenderer = new InGameHotbarRenderer(playerInventory);
        inGameOnlyUIs.add(playerHotbarRenderer);
    }

    public UI2() {
        uiCamera = new OrthographicCamera((float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight());
        uiCamera.up.set(0.0F, -1.0F, 0.0F);
        uiCamera.direction.set(0.0F, 0.0F, 1.0F);
        uiCamera.update();
        uiViewport = new ExtendViewport(800.0F, 600.0F, this.uiCamera);
        uiViewport.apply();
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height);
    }

    @Override
    public void render() {

        if(GameState.currentGameState instanceof InGame) {
            if (Controls.toggleHideUIPressed()) renderUI = !renderUI;
            if (Controls.debugInfoPressed()) renderDebugInfo = !renderDebugInfo;
            if (Controls.inventoryPressed()) {
                if(areAnyInventoriesOpen()) {
                    closeAnyOpenInventories();
                } else {
                    openPlayerInventory(false);
                }
            };
            if (Controls.cycleItemLeft()) playerInventory.moveSelectedHotbarSlot(-1);
            if (Controls.cycleItemRight()) playerInventory.moveSelectedHotbarSlot(-1);
            if(CraterChat.Chat.chatKeybind.isJustPressed() && !CraterChat.Chat.isOpen()) CraterChat.Chat.toggle();
        }


        mouseOverUI = uiNeedMouse = renderUI && !activeInGameUIs.isEmpty();

        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        uiViewport.unproject(mouse);

        Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        uiViewport.apply();
        batch.setProjectionMatrix(uiCamera.combined);

        if(renderUI) {
            crosshair.render(uiCamera);

            List<IItemInventoryRenderer> inventories = new ArrayList<>();
            for(InGameUI ui : activeInGameUIs) {
                if(ui instanceof IItemInventoryRenderer inventoryRenderer) {
                    inventories.add(inventoryRenderer);
                } else if(ui instanceof BlockInventoryUI blockUI) {
                    inventories.add(blockUI.renderer);
                }
            }
            for(IItemInventoryRenderer activeInventory : inventories) {
                if(uiNeedMouse && Gdx.input.isButtonJustPressed(0) && activeInventory != mouseInventoryRenderer) {
                    BaseItemElement atMouse = activeInventory.atMouse(uiViewport, mouse);
                    if(atMouse != null) ItemInventory.swapSlots(atMouse.slot, MouseInventory.mouseInventory.slot);
                }
            }
            List<Component> uiElements = new ArrayList<>();
            if(activeInGameUIs.isEmpty()) {
                for(InGameUI ui : inGameOnlyUIs) {
                    uiElements.addAll(ui.render(uiViewport, uiCamera, mouse));
                    uiViewport.apply();
                }
            }
            for(InGameUI ui : activeInGameUIs) {
                uiElements.addAll(ui.render(uiViewport, uiCamera, mouse));
                uiViewport.apply();
            }
            for(InGameUI ui : alwaysActiveUIs) {
                uiElements.addAll(ui.render(uiViewport, uiCamera, mouse));
                uiViewport.apply();
            }
            UIRenderer.uiRenderer.render(uiElements, uiCamera, uiViewport, mouse);

            if (renderDebugInfo) {
                batch.begin();
                DebugInfo.drawDebugText(batch, uiViewport);
                batch.end();
            }

            CraterChat.Chat.render(uiViewport, uiCamera);
        }

        if(ChunkSaver.isSaving) {
            batch.begin();
            FontRenderer.drawText(batch, uiViewport, "Auto Saving World", -5, -5, HorizontalAnchor.RIGHT_ALIGNED, VerticalAnchor.BOTTOM_ALIGNED);
            batch.end();
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            playerInventory.setSelectedHotbarSlot(keycode - Input.Keys.NUM_1);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        playerInventory.moveSelectedHotbarSlot((int) amountY);
        return true;
    }
}
