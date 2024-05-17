package net.paxyinc.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.api.gui.ProgressBarElement;
import dev.crmodders.flux.engine.GameLoader;
import dev.crmodders.flux.localization.TranslationKey;
import dev.crmodders.flux.ui.Component;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import net.paxyinc.entities.BetterEntity;

import java.util.ArrayList;
import java.util.List;

public class PlayerUI implements InGameUI {

    private List<Component> components = new ArrayList<>();
    private final Player player;

    private ProgressBarElement healthBar;

    public PlayerUI(Player player) {
        this.player = player;

        healthBar = new ProgressBarElement();
        healthBar.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        healthBar.setBounds(0F, -72F, 256F, 12.0F);
        components.add(healthBar);
    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        BetterEntity entity = (BetterEntity) player.getEntity();
        healthBar.setRange(100);
        healthBar.setProgress(entity.health());
        return components;
    }
}
