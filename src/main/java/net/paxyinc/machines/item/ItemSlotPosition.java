package net.paxyinc.machines.item;

import com.badlogic.gdx.math.Rectangle;

public class ItemSlotPosition {
    public final Rectangle rectangle;
    public final ItemSlot slot;
    public boolean hovered = false, selected = false, visible = true;

    public ItemSlotPosition(Rectangle rectangle, ItemSlot slot) {
        this.rectangle = rectangle;
        this.slot = slot;
    }
}