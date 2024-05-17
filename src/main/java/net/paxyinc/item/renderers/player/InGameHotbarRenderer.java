package net.paxyinc.item.renderers.player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.api.gui.base.BaseText;
import dev.crmodders.flux.ui.Component;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import net.paxyinc.item.IItemInventoryRenderer;
import net.paxyinc.item.ItemInventory;
import net.paxyinc.item.ItemSlot;
import net.paxyinc.item.inventories.PlayerInventory;
import net.paxyinc.item.renderers.BasicInventoryRenderer;
import net.paxyinc.ui.elements.BaseItemElement;
import net.paxyinc.ui.elements.InventoryBackgroundElement;
import net.paxyinc.util.BlockNameUtil;

import java.util.List;

public class InGameHotbarRenderer extends BasicInventoryRenderer<PlayerInventory> {

    private List<BaseItemElement> hotbar;

    private BaseText selectedItemText;

    public InGameHotbarRenderer(PlayerInventory inventory) {
        super(inventory);
        hotbar = createArea(PlayerInventory.HOTBAR, PlayerInventory.HOTBAR + 9);

        layoutAreaAroundCenterPoint(hotbar, 9, 0, -32, 2, 32);

        setMouseHover(hotbar, false);
        setAlignment(hotbar, HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        setVisible(hotbar, true);

        selectedItemText = new BaseText();
        selectedItemText.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        selectedItemText.setBounds(0, -64, 320, 48);
        selectedItemText.text = "";
        selectedItemText.backgroundEnabled = false;
        selectedItemText.soundEnabled = false;
        selectedItemText.repaint();
    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        ItemSlot selected = inventory.getSelectedItem();

        resetFlags(hotbar);
        setVisible(hotbar, true);
        for(BaseItemElement pos : hotbar) {
            if(pos.slot.slotId == selected.slotId) pos.isSelected = true;
        }

        if(selected.itemStack != null) {
            selectedItemText.text = BlockNameUtil.getNiceName(selected.itemStack.item.name);
            selectedItemText.repaint();
        } else {
            selectedItemText.text = "";
            selectedItemText.repaint();
        }

        List<Component> ui = super.render(uiViewport, uiCamera, mouse);
        ui.add(selectedItemText);
        return ui;
    }
}
