package net.paxyinc.machines.item.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;
import net.paxyinc.machines.item.ItemInventory;
import net.paxyinc.machines.ui.BaseItemElement;
import net.paxyinc.machines.ui.InventoryBackgroundElement;

import java.util.List;

public class FurnaceInventoryRenderer extends BasicInventoryRenderer {

    private List<BaseItemElement> slots;
    private InventoryBackgroundElement background;

    public FurnaceInventoryRenderer(ItemInventory inventory) {
        super(inventory);
        slots = createArea(0, 3);
        background = new InventoryBackgroundElement();
        background.color = Color.DARK_GRAY;
    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        layoutAreaAroundCenterPoint(slots ,3, 0, -72F, 2, 32);
        resetFlags(slots);
        setVisible(slots, true);

        List<Component> ui = super.render(uiViewport, uiCamera, mouse);
        background.resize(slots, uiViewport, 12F);
        ui.add(0, background);
        return ui;
    }
}
