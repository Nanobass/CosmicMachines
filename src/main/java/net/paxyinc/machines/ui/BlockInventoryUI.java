package net.paxyinc.machines.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;
import net.paxyinc.machines.item.ItemInventory;

import java.util.List;

public class BlockInventoryUI implements InGameUI {

    protected ItemInventory inventory;

    public BlockInventoryUI(ItemInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        return inventory.renderer.render(uiViewport, uiCamera, mouse);
    }
}
