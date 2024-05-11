package net.paxyinc.machines.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.api.gui.base.BaseElement;
import dev.crmodders.flux.ui.UIRenderer;
import dev.crmodders.flux.ui.shapes.ShapeBatch;
import dev.crmodders.flux.ui.shapes.ShapeBatchBuilder;
import dev.crmodders.flux.ui.text.TextBatch;
import dev.crmodders.flux.ui.util.ChildViewport;
import net.paxyinc.machines.item.IItemView;
import net.paxyinc.machines.item.ItemSlot;
import net.paxyinc.machines.util.BlockNameUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static dev.crmodders.flux.ui.UIRenderer.uiRenderer;

public class BaseItemElement extends BaseElement {


    protected static Color itemBackgroundColor = new Color(0.54f, 0.54f, 0.54f, 1.0f);
    protected static Color itemHighlightedColor = new Color(0.64f, 0.64f, 0.64f, 1.0f);

    protected static PerspectiveCamera itemCamera;
    protected static ChildViewport itemViewport;
    protected static Vector2 tmp = new Vector2();

    static {
        itemCamera = new PerspectiveCamera(1.1F, 100.0F, 100.0F);
        itemCamera.near = 30.0F;
        itemCamera.far = 100.0F;
        itemCamera.position.set(50.0F, 50.0F, 50.0F);
        itemCamera.lookAt(0.5F, 0.5F, 0.5F);
        itemCamera.update();
        itemViewport = new ChildViewport(new FitViewport(100.0F, 100.0F, itemCamera));
    }

    public ItemSlot slot;
    public boolean shouldRenderBackground = true, shouldRenderAmount = true, shouldRenderName = true;
    public boolean isHoveredOver, isSelected;
    private TextBatch name, amount;
    private ShapeBatch background, hovered, selected;

    public BaseItemElement(ItemSlot slot) {
        this.slot = slot;
    }

    @Override
    public void update(UIRenderer renderer, Viewport viewport, Vector2 mouse) {
        repaint();
        super.update(renderer, viewport, mouse);
    }

    @Override
    public void paint(UIRenderer renderer) {
        ShapeBatchBuilder background = new ShapeBatchBuilder();
        background.color(itemBackgroundColor);
        background.fillRect(0, 0, width, height);
        this.background = background.build();

        ShapeBatchBuilder hovered = new ShapeBatchBuilder();
        hovered.color(itemHighlightedColor);
        hovered.fillRect(0, 0, width, height);
        this.hovered = hovered.build();

        ShapeBatchBuilder selected = new ShapeBatchBuilder();
        selected.color(itemHighlightedColor);
        selected.lineThickness(4);
        selected.drawRect(0, 0, width, height);
        this.selected = selected.build();

        if(slot.itemStack != null) {
            name = renderer.createText(BlockNameUtil.getNiceName(slot.itemStack.item.name), Color.WHITE);
            amount = renderer.createText(UIRenderer.font, 12.0F, String.valueOf(slot.itemStack.amount), Color.WHITE);
        } else {
            name = null;
            amount = null;
        }
    }

    @Override
    public void drawBackground(UIRenderer renderer, Viewport viewport) {
        if(!visible) return;
        float x = getDisplayX(viewport);
        float y = getDisplayY(viewport);
        if(shouldRenderBackground) {
            if(isHoveredOver) hovered.render(uiRenderer, x, y);
            else background.render(uiRenderer, x, y);
        }
    }

    @Override
    public void draw(UIRenderer renderer, Viewport uiViewport) {
        if(!visible) return;
        float x = getDisplayX(uiViewport);
        float y = getDisplayY(uiViewport);
        if(slot.itemStack != null) {
            renderer.prepareForCustomShader();
            itemViewport.setScreenBounds(uiViewport, x, y + height, width, height);
            itemViewport.apply();
            {
                Gdx.gl.glEnable(GL11.GL_CULL_FACE);
                Gdx.gl.glCullFace(GL11.GL_BACK);
                Gdx.gl.glEnable(GL11.GL_DEPTH_TEST);
                Gdx.gl.glDepthFunc(GL11.GL_ALWAYS);
                slot.itemStack.item.view.render(itemCamera, itemCamera.combined);
                Gdx.gl.glEnable(GL11.GL_DEPTH_TEST);
                Gdx.gl.glDepthFunc(GL11.GL_LESS);
                Gdx.gl.glEnable(GL11.GL_CULL_FACE);
                Gdx.gl.glCullFace(GL11.GL_BACK);
                Gdx.gl.glActiveTexture(GL13.GL_TEXTURE0);
            }
            uiViewport.apply();
            renderer.resetAfterCustomShader();
        }
    }

    @Override
    public void drawOverlay(UIRenderer renderer, Viewport viewport) {
        if(!visible) return;
        float x = getDisplayX(viewport);
        float y = getDisplayY(viewport);
        if (shouldRenderBackground && isSelected) selected.render(uiRenderer, x, y);
        if(slot.itemStack != null) {
            if (shouldRenderName && isHoveredOver) name.render(renderer, x, y + 32.0F);
            if (shouldRenderAmount && slot.itemStack.amount > 1) amount.render(renderer, x, y + 18.0F);
        }
    }

    @Override
    public void onMouseEnter() {
        isHoveredOver = true;
    }

    @Override
    public void onMouseExit() {
        isHoveredOver = false;
    }
}
