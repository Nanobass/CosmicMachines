package net.paxyinc.item.renderers.player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;
import net.paxyinc.item.ItemInventory;
import net.paxyinc.item.ItemSlot;
import net.paxyinc.item.inventories.PlayerInventory;
import net.paxyinc.item.renderers.BasicInventoryRenderer;
import net.paxyinc.ui.elements.BaseItemElement;
import net.paxyinc.ui.elements.InventoryBackgroundElement;
import net.paxyinc.util.BlockNameUtil;

import java.util.List;

public class PlayerInventoryRenderer extends BasicInventoryRenderer<PlayerInventory> {

    private List<BaseItemElement> hotbar, main, armor;

    private InventoryBackgroundElement background;

    public PlayerInventoryRenderer(PlayerInventory inventory) {
        super(inventory);
        hotbar = createArea(PlayerInventory.HOTBAR, PlayerInventory.HOTBAR + 9);
        main = createArea(PlayerInventory.INVENTORY, PlayerInventory.INVENTORY + 27);
        armor = createArea(PlayerInventory.ARMOR, PlayerInventory.ARMOR + 4);

        setVisible(hotbar, true);
        setVisible(main, true);
        setVisible(armor, true);

        layoutAreaAroundCenterPoint(hotbar, 9,0, 80, 2, 32);
        layoutAreaAroundCenterPoint(main, 9, 0, 0, 2, 32);
        layoutAreaAroundCenterPoint(armor, 1, -192F, -16F, 2, 32);

        background = new InventoryBackgroundElement();
        background.color = Color.DARK_GRAY;
    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {

        background.resize(positions, uiViewport, 12F);

        List<Component> ui = super.render(uiViewport, uiCamera, mouse);
        ui.add(0, background);
        return ui;
    }

}
