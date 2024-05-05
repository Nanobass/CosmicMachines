package net.paxyinc.machines.mixins;

import dev.crmodders.flux.FluxConstants;
import dev.crmodders.flux.api.gui.ProgressBarElement;
import dev.crmodders.flux.engine.GameLoader;
import dev.crmodders.flux.menus.BasicMenu;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.gamestates.GameState;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Queue;

@Mixin(GameLoader.class)
public abstract class GameLoaderMixin extends BasicMenu  {

    @Shadow
    public ProgressBarElement ram;
    @Shadow private Queue<Runnable> glQueue;
    @Overwrite
    public void render(float partTime) {
        Runtime runtime = Runtime.getRuntime();
        this.ram.value = (int)((runtime.totalMemory() - runtime.freeMemory()) / 1048576L);
        this.ram.range = (int)(runtime.maxMemory() / 1048576L);
        this.ram.updateText();
        long endTime = System.currentTimeMillis() + 50L;

        while(!this.glQueue.isEmpty() && System.currentTimeMillis() < endTime) {
            Runnable glTask = (Runnable)this.glQueue.poll();
            glTask.run();
        }

        if (FluxConstants.FluxHasLoaded) {
            AccessableRegistry<Runnable> itemFinalizers = ItemRegistry.ITEM_FINALIZERS.access();
            for(Identifier itemId : itemFinalizers.getRegisteredNames()) {
                itemFinalizers.get(itemId).run();
            }
            GameState.switchToGameState(FluxConstants.MAIN_MENU);
        }

        super.render(partTime);
    }

}
