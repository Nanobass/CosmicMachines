package net.paxyinc.machines.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.api.gui.base.BaseElement;
import dev.crmodders.flux.ui.UIRenderer;
import dev.crmodders.flux.ui.shapes.ShapeBatch;
import dev.crmodders.flux.ui.shapes.ShapeBatchBuilder;

public class BaseRectangleElement extends BaseElement {

    public Color color = Color.WHITE;

    private ShapeBatch batch;

    @Override
    public void paint(UIRenderer renderer) {
        ShapeBatchBuilder batch = new ShapeBatchBuilder();
        batch.color(color);
        batch.fillRect(0, 0, width, height);
        this.batch = batch.build();
    }

    @Override
    public void drawBackground(UIRenderer renderer, Viewport viewport) {
        if(!visible) return;
        float x = getDisplayX(viewport);
        float y = getDisplayY(viewport);
        batch.render(renderer, x, y);
    }
}
