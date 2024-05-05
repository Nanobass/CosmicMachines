package net.paxyinc.machines.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL31;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.menus.BasicMenu;
import dev.crmodders.flux.util.PrivUtils;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.items.ItemCatalog;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.Crosshair;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.ui.debug.DebugInfo;
import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.item.ItemSlotPosition;
import net.paxyinc.machines.item.ItemStack;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import net.paxyinc.machines.item.renderers.PlayerInventoryRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

public class InGameUI extends UI {

    public Viewport uiViewport;
    public OrthographicCamera uiCamera;
    public ShapeRenderer shapeRenderer = new ShapeRenderer();
    public SpriteBatch batch = new SpriteBatch();
    public Crosshair crosshair = new Crosshair();

    public boolean renderDebugInfo = false;

    public InGameUI() {
        uiCamera = new OrthographicCamera((float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight());
        uiViewport = new ExtendViewport(800.0F, 600.0F, this.uiCamera);
        uiCamera.up.set(0.0F, -1.0F, 0.0F);
        uiCamera.direction.set(0.0F, 0.0F, 1.0F);
        uiCamera.update();
        uiViewport.apply(false);
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height);
    }

    @Override
    public void render() {
        if (Controls.toggleHideUIPressed()) renderUI = !renderUI;
        if (Controls.debugInfoPressed()) renderDebugInfo = !renderDebugInfo;
        if (Controls.inventoryPressed()) PlayerInventory.inventory.renderInventory = !PlayerInventory.inventory.renderInventory;
        uiNeedMouse = PlayerInventory.inventory.renderInventory;
        if (Controls.cycleItemLeft()) PlayerInventory.inventory.hotbarIndex = Math.floorMod((PlayerInventory.inventory.hotbarIndex - 1), 9);
        if (Controls.cycleItemRight()) PlayerInventory.inventory.hotbarIndex = Math.floorMod((PlayerInventory.inventory.hotbarIndex + 1), 9);
        UI.mouseOverUI = UI.uiNeedMouse;

        Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        if(renderUI) {
            crosshair.render(uiCamera);
            shapeRenderer.setProjectionMatrix(uiCamera.combined);

            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();

            ItemSlotPosition hoveredSlot = PlayerInventory.inventory.renderer.atMouse(uiViewport, mouseX, mouseY);
            ItemSlotPosition selectedSlot = PlayerInventory.inventory.renderer.atSlot(PlayerInventory.inventory.hotbarIndex);

            if(hoveredSlot != null) hoveredSlot.hovered = true;
            selectedSlot.selected = true;

            PlayerInventory.inventory.renderer.render(uiViewport, uiCamera);

            if(hoveredSlot != null) hoveredSlot.hovered = false;
            selectedSlot.selected = false;

            if (renderDebugInfo) {
                Gdx.gl.glActiveTexture(GL13.GL_TEXTURE0);
                batch.setProjectionMatrix(uiCamera.combined);
                batch.begin();
                DebugInfo.drawDebugText(batch, uiViewport);
                batch.end();
            }

        }
    }

    @Override
    public boolean keyDown(int keycode) {
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
        PlayerInventory.inventory.hotbarIndex = Math.floorMod((PlayerInventory.inventory.hotbarIndex + (int)amountY), 9);
        return true;
    }
}
