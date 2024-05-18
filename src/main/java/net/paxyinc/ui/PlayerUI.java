package net.paxyinc.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
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
    private ProgressBarElement oxygenBar;

    public PlayerUI(Player player) {
        this.player = player;

        healthBar = new ProgressBarElement();
        healthBar.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        healthBar.setBounds(0F, -72F, 256F, 12.0F);
        components.add(healthBar);

        oxygenBar = new ProgressBarElement();
        oxygenBar.setAnchors(HorizontalAnchor.CENTERED, VerticalAnchor.BOTTOM_ALIGNED);
        oxygenBar.setBounds(0F, -92F, 256F, 12.0F);
        oxygenBar.progressColor = Color.CYAN;
        components.add(oxygenBar);

    }

    @Override
    public List<Component> render(Viewport uiViewport, Camera uiCamera, Vector2 mouse) {
        BetterEntity entity = (BetterEntity) player.getEntity();
        healthBar.setRange(100);
        healthBar.setProgress(entity.health);
        oxygenBar.setRange(100);
        oxygenBar.setProgress(entity.oxygen);
        return components;
    }
}
