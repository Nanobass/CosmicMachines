package net.paxyinc.item.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;
import net.paxyinc.item.ItemInventory;
import net.paxyinc.item.inventories.FurnaceInventory;
import net.paxyinc.ui.elements.BaseItemElement;
import net.paxyinc.ui.elements.InventoryBackgroundElement;

import java.util.List;

public class FurnaceInventoryRenderer extends BasicInventoryRenderer<ItemInventory> {

    private List<BaseItemElement> slots;
    private InventoryBackgroundElement background;

    public FurnaceInventoryRenderer(ItemInventory inventory) {
        super(inventory);
        slots = createArea(0, 3);
        layoutAreaAroundCenterPoint(slots ,3, 0, -72F, 2, 32);
        setVisible(slots, true);

        background = new InventoryBackgroundElement();
        background.color = Color.DARK_GRAY;
    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        background.resize(slots, uiViewport, 12F);

        List<Component> ui = super.render(uiViewport, uiCamera, mouse);
        ui.add(0, background);
        return ui;
    }
}
