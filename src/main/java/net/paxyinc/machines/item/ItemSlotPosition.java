package net.paxyinc.machines.item;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class ItemSlotPosition {

    public final ItemSlot slot;
    public Rectangle rectangle;
    public ItemSlotPositionState state;

    public ItemSlotPosition(ItemSlot slot) {
        this.slot = slot;
        this.rectangle = new Rectangle();
        this.state = ItemSlotPositionState.DISABLED;
    }
}