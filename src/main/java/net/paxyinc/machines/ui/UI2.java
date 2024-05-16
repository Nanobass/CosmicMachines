package net.paxyinc.machines.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.insertsoda.craterchat.CraterChat;
import dev.crmodders.flux.ui.Component;
import dev.crmodders.flux.ui.UIRenderer;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.gamestates.PauseMenu;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.Crosshair;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.ui.debug.DebugInfo;
import net.paxyinc.machines.item.IItemInventoryRenderer;
import net.paxyinc.machines.item.ItemInventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static net.paxyinc.machines.item.inventories.MouseInventory.mouseInventory;
import static net.paxyinc.machines.item.inventories.PlayerInventory.inventory;

public class UI2 extends UI {

    public Viewport uiViewport;
    public OrthographicCamera uiCamera;
    public Crosshair crosshair = new Crosshair();

    public static List<InGameUI> activeInGameUIs = new ArrayList<>();
    public static InGameUI activeBlockUI = null;

    public boolean renderDebugInfo = false;

    public static void openBlockUI(InGameUI ui) {
        if(activeBlockUI != null) {
            activeInGameUIs.remove(activeBlockUI);
        }
        activeBlockUI = ui;
        activeInGameUIs.add(ui);
    }

    public static void closeBlockUI() {
        if(activeBlockUI != null) {
            activeInGameUIs.remove(activeBlockUI);
            activeBlockUI = null;
        }
    }

    public static boolean areAnyInventoriesOpen() {
        return inventory.renderInventory || activeBlockUI != null;
    }

    public static boolean closeAnyOpenInventories() {
        boolean wereAnyInventoriesOpen = false;
        if(inventory.renderInventory) {
            wereAnyInventoriesOpen = true;
            inventory.renderInventory = false;
        }
        if(activeBlockUI != null) {
            wereAnyInventoriesOpen = true;
            closeBlockUI();
        }
        return wereAnyInventoriesOpen;
    }

    public UI2() {
        uiCamera = new OrthographicCamera((float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight());
        uiCamera.up.set(0.0F, -1.0F, 0.0F);
        uiCamera.direction.set(0.0F, 0.0F, 1.0F);
        uiCamera.update();
        uiViewport = new ExtendViewport(800.0F, 600.0F, this.uiCamera);
        uiViewport.apply();

        activeInGameUIs.add(inventory.renderer);
        activeInGameUIs.add(mouseInventory.renderer);

    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height);
    }

    @Override
    public void render() {
        if(!PauseMenu.class.isInstance(GameState.currentGameState)) {
            if (Controls.toggleHideUIPressed()) renderUI = !renderUI;
            if (Controls.debugInfoPressed()) renderDebugInfo = !renderDebugInfo;
            if (Controls.inventoryPressed()) {
                if(areAnyInventoriesOpen()) {
                    closeAnyOpenInventories();
                } else {
                    inventory.renderInventory = true;
                }
            };
            if (Controls.cycleItemLeft()) inventory.moveSelectedHotbarSlot(-1);
            if (Controls.cycleItemRight()) inventory.moveSelectedHotbarSlot(-1);
            if(CraterChat.Chat.chatKeybind.isJustPressed() && !CraterChat.Chat.isOpen() && GameState.currentGameState instanceof InGame){
                CraterChat.Chat.toggle();
            }
        }


        mouseOverUI = uiNeedMouse = renderUI && (inventory.renderInventory || activeBlockUI != null);

        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        uiViewport.unproject(mouse);

        Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        if(renderUI) {
            crosshair.render(uiCamera);
            uiViewport.apply();
            batch.setProjectionMatrix(uiCamera.combined);

            inventory.renderChestMode = activeBlockUI != null;

            List<ItemInventory> inventories = new ArrayList<>();
            for(InGameUI ui : activeInGameUIs) {
                if(ui instanceof IItemInventoryRenderer inventoryRenderer) {
                    inventories.add(inventoryRenderer.getInventory());
                } else if(ui instanceof BlockInventoryUI blockUI) {
                    inventories.add(blockUI.inventory);
                }
            }
            for(ItemInventory activeInventory : inventories) {
                if(uiNeedMouse && Gdx.input.isButtonJustPressed(0) && activeInventory != mouseInventory) {
                    BaseItemElement atMouse = activeInventory.renderer.atMouse(uiViewport, mouse);
                    if(atMouse != null) ItemInventory.swapSlots(atMouse.slot, mouseInventory.slot);
                }
            }
            List<Component> uiElements = new ArrayList<>();
            for(InGameUI ui : activeInGameUIs) {
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
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            inventory.setSelectedHotbarSlot(keycode - Input.Keys.NUM_1);
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
        inventory.moveSelectedHotbarSlot((int) amountY);
        return true;
    }
}
