package net.paxyinc.machines.item.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.UIRenderer;
import net.paxyinc.machines.item.IItemInventoryRenderer;
import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.item.ItemSlotPosition;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.*;

public class PlayerInventoryRenderer extends BasicInventoryRenderer {

    public PlayerInventoryRenderer(ItemInventory inventory) {
        super(inventory);
    }

    @Override
    public void render(Viewport uiViewport, Camera uiCamera) {
        for(ItemSlotPosition pos : positions) pos.visible = false;
        if(PlayerInventory.inventory.renderInventory) {
            layoutAreaAroundCenterPoint(PlayerInventory.HOTBAR, PlayerInventory.HOTBAR + 9 ,0, 80, true);
            layoutAreaAroundCenterPoint(PlayerInventory.INVENTORY, PlayerInventory.INVENTORY + 27, 0, 0, true);
            layoutAreaAroundCenterPoint(PlayerInventory.ARMOR, PlayerInventory.ARMOR + 4, 1, -192F, -16F, 2, 32, true);
        } else {
            layoutAreaAroundCenterPoint(PlayerInventory.HOTBAR, PlayerInventory.HOTBAR + 9 ,0, uiViewport.getWorldHeight() / 2.0F - 36F, PlayerInventory.inventory.renderHotbar);
        }
        super.render(uiViewport, uiCamera);
    }
}
