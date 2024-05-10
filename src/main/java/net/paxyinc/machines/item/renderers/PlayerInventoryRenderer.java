package net.paxyinc.machines.item.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.UIRenderer;
import net.paxyinc.machines.item.*;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import net.paxyinc.machines.ui.BaseItemElement;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.*;
import java.util.List;

public class PlayerInventoryRenderer extends BasicInventoryRenderer {

    private List<BaseItemElement> hotbarInGame, hotbarInInventory, mainInventory, armorInInventory;

    public PlayerInventoryRenderer(ItemInventory inventory) {
        super(inventory);
        hotbarInGame = createArea(PlayerInventory.HOTBAR, PlayerInventory.HOTBAR + 9);
        hotbarInInventory = createArea(PlayerInventory.HOTBAR, PlayerInventory.HOTBAR + 9);
        mainInventory = createArea(PlayerInventory.INVENTORY, PlayerInventory.INVENTORY + 27);
        armorInInventory = createArea(PlayerInventory.ARMOR, PlayerInventory.ARMOR + 4);
    }

    @Override
    public void render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        layoutAreaAroundCenterPoint(hotbarInGame ,9, 0, uiViewport.getWorldHeight() / 2.0F - 36F, 2, 32);
        layoutAreaAroundCenterPoint(hotbarInInventory, 9,0, 80, 2, 32);
        layoutAreaAroundCenterPoint(mainInventory, 9, 0, 0, 2, 32);
        layoutAreaAroundCenterPoint(armorInInventory, 1, -192F, -16F, 2, 32);

        setState(positions, ItemSlotPositionState.DISABLED);
        if(PlayerInventory.inventory.renderInventory) {
            setState(hotbarInInventory, ItemSlotPositionState.VISIBLE);
            setState(mainInventory, ItemSlotPositionState.VISIBLE);
            setState(armorInInventory, ItemSlotPositionState.VISIBLE);
        } else if(PlayerInventory.inventory.renderHotbar) {
            setState(hotbarInGame, ItemSlotPositionState.VISIBLE);
        }

        BaseItemElement atMouse = atMouse(mouse);
        if(atMouse != null) {
            orState(atMouse.slot, ItemSlotPositionState.HOVERED);
        }
        orState(PlayerInventory.inventory.getSelectedItem(), ItemSlotPositionState.SELECTED);
        super.render(uiViewport, uiCamera, mouse);
    }
}
