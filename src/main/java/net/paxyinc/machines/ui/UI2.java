package net.paxyinc.machines.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.insertsoda.craterchat.CraterChat;
import dev.crmodders.flux.ui.util.ChildViewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.Crosshair;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.ui.debug.DebugInfo;
import net.paxyinc.machines.item.*;
import net.paxyinc.machines.item.inventories.MouseInventory;
import net.paxyinc.machines.item.renderers.MouseInventoryRenderer;
import org.checkerframework.checker.units.qual.C;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.List;

import static net.paxyinc.machines.item.inventories.PlayerInventory.inventory;

public class UI2 extends UI {

    public Viewport uiViewport;
    public OrthographicCamera uiCamera;
    public Crosshair crosshair = new Crosshair();

    public static List<InGameUI> inGameUIs = new ArrayList<>();

    public boolean renderDebugInfo = false;

    public UI2() {
        uiCamera = new OrthographicCamera((float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight());
        uiCamera.up.set(0.0F, -1.0F, 0.0F);
        uiCamera.direction.set(0.0F, 0.0F, 1.0F);
        uiCamera.update();
        uiViewport = new ExtendViewport(800.0F, 600.0F, this.uiCamera);
        uiViewport.apply();

        inGameUIs.add(inventory.renderer.getUI());
        inGameUIs.add(MouseInventory.mouseInventory.renderer.getUI());

    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height);
    }

    @Override
    public void render() {
        if (Controls.toggleHideUIPressed()) renderUI = !renderUI;
        if (Controls.debugInfoPressed()) renderDebugInfo = !renderDebugInfo;
        if (Controls.inventoryPressed()) inventory.renderInventory = !inventory.renderInventory;
        if (Controls.cycleItemLeft()) inventory.moveSelectedHotbarSlot(-1);
        if (Controls.cycleItemRight()) inventory.moveSelectedHotbarSlot(-1);
        if(CraterChat.Chat.chatKeybind.isJustPressed() && !CraterChat.Chat.isOpen() && GameState.currentGameState instanceof InGame){
            CraterChat.Chat.toggle();
        }
        mouseOverUI = uiNeedMouse = inventory.renderInventory;

        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        uiViewport.unproject(mouse);

        Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        if(renderUI) {
            crosshair.render(uiCamera);

            if(uiNeedMouse && Gdx.input.isButtonJustPressed(0)) {
                BaseItemElement atMouse = inventory.renderer.atMouse(mouse);
                if(atMouse != null) ItemInventory.swapSlots(atMouse.slot, MouseInventory.mouseInventory.slots.get(0));
            }

            uiViewport.apply();
            batch.setProjectionMatrix(uiCamera.combined);

            for(InGameUI ui : inGameUIs) {
                ui.render(uiViewport, uiCamera, mouse);
                uiViewport.apply();
            }

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
