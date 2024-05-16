package net.paxyinc.machines.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

public class InventoryBackgroundElement extends BaseRectangleElement {

    public void resize(List<BaseItemElement> slots, Viewport uiViewport, float padding) {
        BaseItemElement first = slots.get(0);
        Rectangle rect = new Rectangle(first.getDisplayX(uiViewport), first.getDisplayY(uiViewport), first.width, first.height);
        for(BaseItemElement element : slots) {
            Rectangle elementRect = new Rectangle(element.getDisplayX(uiViewport), element.getDisplayY(uiViewport), element.width, element.height);
            rect.merge(elementRect);
        }
        rect.merge(rect.x - padding, rect.y - padding);
        rect.merge(rect.x + rect.width + padding, rect.y + rect.height + padding);
        setBounds(rect.x + rect.width / 2, rect.y + rect.height / 2, rect.width, rect.height);
        repaint();
    }

}
