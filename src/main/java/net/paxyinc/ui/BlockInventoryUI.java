package net.paxyinc.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;
import net.paxyinc.item.IItemInventoryRenderer;

import java.util.List;

public class BlockInventoryUI implements InGameUI {

    protected IItemInventoryRenderer renderer;

    public BlockInventoryUI(IItemInventoryRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        return renderer.render(uiViewport, uiCamera, mouse);
    }
}
