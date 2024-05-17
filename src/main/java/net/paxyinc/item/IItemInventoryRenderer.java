package net.paxyinc.item;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.paxyinc.ui.InGameUI;
import net.paxyinc.ui.elements.BaseItemElement;

public interface IItemInventoryRenderer extends InGameUI {

    BaseItemElement atMouse(Viewport viewport, Vector2 mouse);

    ItemInventory getInventory();

}
