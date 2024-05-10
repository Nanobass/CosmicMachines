package net.paxyinc.machines.item.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.api.gui.base.BaseElement;
import dev.crmodders.flux.api.gui.base.BaseText;
import dev.crmodders.flux.ui.Component;
import dev.crmodders.flux.ui.UIRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import net.paxyinc.machines.item.*;
import net.paxyinc.machines.item.inventories.PlayerInventory;
import net.paxyinc.machines.ui.BaseItemElement;
import net.paxyinc.machines.ui.BaseRectangleElement;
import net.paxyinc.machines.ui.InventoryBackgroundElement;
import net.paxyinc.machines.util.BlockNameUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerInventoryRenderer extends BasicInventoryRenderer {

    private List<BaseItemElement> hotbarInGame, hotbarInInventory, mainInventory, armorInInventory;

    private BaseText selectedItemText;
    private InventoryBackgroundElement background;

    public PlayerInventoryRenderer(ItemInventory inventory) {
        super(inventory);
        hotbarInGame = createArea(PlayerInventory.HOTBAR, PlayerInventory.HOTBAR + 9);
        hotbarInInventory = createArea(PlayerInventory.HOTBAR, PlayerInventory.HOTBAR + 9);
        mainInventory = createArea(PlayerInventory.INVENTORY, PlayerInventory.INVENTORY + 27);
        armorInInventory = createArea(PlayerInventory.ARMOR, PlayerInventory.ARMOR + 4);

        selectedItemText = new BaseText();
        selectedItemText.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        selectedItemText.setBounds(0, -64, 320, 48);
        selectedItemText.text = "";
        selectedItemText.backgroundEnabled = false;
        selectedItemText.soundEnabled = false;
        selectedItemText.repaint();

        background = new InventoryBackgroundElement();
        background.color = Color.DARK_GRAY;

    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        layoutAreaAroundCenterPoint(hotbarInGame ,9, 0, uiViewport.getWorldHeight() / 2.0F - 36F, 2, 32);
        float yOffset = PlayerInventory.inventory.renderChestMode ? 92 : 0;
        layoutAreaAroundCenterPoint(hotbarInInventory, 9,0, 80 + yOffset, 2, 32);
        layoutAreaAroundCenterPoint(mainInventory, 9, 0, 0 + yOffset, 2, 32);
        layoutAreaAroundCenterPoint(armorInInventory, 1, -192F, -16F + yOffset, 2, 32);

        resetFlags(positions);
        background.visible = false;
        if(PlayerInventory.inventory.renderInventory || PlayerInventory.inventory.renderChestMode) {
            setVisible(hotbarInInventory, true);
            setVisible(mainInventory, true);
            setVisible(armorInInventory, true);
            background.visible = true;
        } else if(PlayerInventory.inventory.renderHotbar) {
            setVisible(hotbarInGame, true);
        }

        ItemSlot selected = PlayerInventory.inventory.getSelectedItem();
        for(BaseItemElement pos : hotbarInGame) {
            if(pos.slot.slotId == selected.slotId) pos.isSelected = true;
        }

        if(selected.itemStack != null && !PlayerInventory.inventory.renderInventory && PlayerInventory.inventory.renderHotbar) {
            selectedItemText.text = BlockNameUtil.getNiceName(selected.itemStack.item.name);
            selectedItemText.repaint();
        } else {
            selectedItemText.text = "";
            selectedItemText.repaint();
        }

        List<Component> ui = super.render(uiViewport, uiCamera, mouse);

        List<BaseItemElement> allInventoryItems = new ArrayList<>();
        allInventoryItems.addAll(hotbarInInventory);
        allInventoryItems.addAll(mainInventory);
        allInventoryItems.addAll(armorInInventory);
        background.resize(allInventoryItems, uiViewport, 12F);

        ui.add(0, background);
        ui.add(selectedItemText);
        return ui;
    }

    @Override
    public BaseItemElement atMouse(Viewport viewport, Vector2 mouse) {
        return (PlayerInventory.inventory.renderInventory || PlayerInventory.inventory.renderChestMode) ? super.atMouse(viewport, mouse) : null;
    }
}
