package net.paxyinc.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.ui.Component;

import java.util.List;

public interface InGameUI {
    List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse);
}
