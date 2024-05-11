package net.paxyinc.machines.mixins;

import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.gamestates.LoadingGame;
import net.paxyinc.machines.interfaces.WorldInterface;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameState.class)
public class GameStateMixin {

    @Inject(method = "switchToGameState", at = @At("HEAD"))
    private static void switchToGameState(GameState gameState, CallbackInfo ci) {
        if(gameState instanceof InGame && GameState.currentGameState instanceof LoadingGame) {
            WorldInterface wi = (WorldInterface) InGame.world;
            wi.initialize();
        }
    }

}
