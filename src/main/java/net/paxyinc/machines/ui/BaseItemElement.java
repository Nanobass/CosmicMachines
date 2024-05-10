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
import net.paxyinc.machines.item.ItemSlotPositionState;
import net.paxyinc.machines.util.BlockNameUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.quiltmc.loader.util.sat4j.core.Vec;

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
    public ItemSlotPositionState state = ItemSlotPositionState.VISIBLE;
    public boolean renderAmount, renderName;
    private TextBatch name, amount;
    private ShapeBatch background;

    public BaseItemElement(ItemSlot slot) {
        this.slot = slot;
    }

    public boolean contains(Vector2 p) {
        return p.x >= x && p.x < x + width && p.y >= y && p.y < y + height;
    }

    @Override
    public void paint(UIRenderer renderer) {
        ShapeBatchBuilder background = new ShapeBatchBuilder();
        background.color(itemBackgroundColor);
        background.fillRect(0, 0, width, height);
        this.background = background.build();

        if(slot.itemStack == null) {
            name = null;
            amount = null;
            return;
        }
        renderAmount = slot.itemStack.amount > 1;
        name = renderer.createText(BlockNameUtil.getNiceName(slot.itemStack.item.name), Color.WHITE);
        amount = renderer.createText(UIRenderer.font, 12.0F, String.valueOf(slot.itemStack.amount), Color.WHITE);
    }

    @Override
    public void draw(UIRenderer renderer, Viewport uiViewport) {
        if(state == ItemSlotPositionState.DISABLED) return;
        background.render(uiRenderer, x, y);

        if(slot.itemStack == null) return;

        itemViewport.setScreenBounds(uiViewport, x, y + height , width, height);
        itemViewport.apply();
        {
            Gdx.gl.glEnable(GL11.GL_CULL_FACE);
            Gdx.gl.glCullFace(GL11.GL_BACK);
            Gdx.gl.glEnable(GL11.GL_DEPTH_TEST);
            Gdx.gl.glDepthFunc(GL11.GL_ALWAYS);
            slot.itemStack.item.view.render(itemCamera);
            Gdx.gl.glEnable(GL11.GL_DEPTH_TEST);
            Gdx.gl.glDepthFunc(GL11.GL_LESS);
            Gdx.gl.glEnable(GL11.GL_CULL_FACE);
            Gdx.gl.glCullFace(GL11.GL_BACK);
            Gdx.gl.glActiveTexture(GL13.GL_TEXTURE0);
            Gdx.gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
        uiViewport.apply();
        renderer.resetAfterCustomShader();

        if(renderName) name.render(renderer, x, y + 32.0F);
        if(renderAmount) amount.render(renderer, x, y + 18.0F);
    }

    @Override
    public void onMouseEnter() {
        renderName = true;
    }

    @Override
    public void onMouseExit() {
        renderName = false;
    }
}
