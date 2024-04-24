package org.example.mod;

import com.badlogic.gdx.files.FileHandle;
import dev.crmodders.flux.api.v5.events.GameEvents;
import dev.crmodders.flux.api.v5.resource.AssetLoader;
import dev.crmodders.flux.api.v5.resource.ResourceLocation;
import dev.crmodders.flux.localization.LanguageFile;
import dev.crmodders.flux.localization.TranslationApi;
import dev.crmodders.flux.logging.LogWrapper;
import dev.crmodders.flux.registry.FluxRegistries;
import dev.crmodders.flux.tags.Identifier;
import net.fabricmc.api.ModInitializer;
import org.example.mod.blocks.BasicExampleBlock;
import org.example.mod.blocks.ResourceExampleBlock;

public class ExampleMod implements ModInitializer {

    public static String MOD_ID = "examplemod";
    private static String TAG = createTag("INIT");

    @Override
    public void onInitialize() {
        LogWrapper.info("%s: Example Mod Initialized".formatted(TAG));

        registerBlocks();
//        registerEventListeners(); // No Language File Created, Look At https://github.com/CRModders/Flux-Translations for more info

    }

    // Create Logging Tag
    public static String createTag(String name) {
        return "\u001b[35;1m{"+MOD_ID+"/"+name+"}\u001b[0m\u001b[37m";
    }

    public static void registerLanguages() {
        GameEvents.ON_REGISTER_LANGUAGE.register(() -> {
            FileHandle file = AssetLoader.unsafeLoadAsset(new ResourceLocation(MOD_ID, "langs/en-us.json")).handle;
            LanguageFile lang = LanguageFile.loadLanguageFile(file);

            TranslationApi.registerLanguageFile(lang);
        });
    }

    public static void registerBlocks() {

        // Registers Block1 with flux
        FluxRegistries.BLOCKS.register(
                new Identifier(MOD_ID, "example_block_one"),
                new BasicExampleBlock()
        );

        // Registers Block2 with flux
        FluxRegistries.BLOCKS.register(
                new Identifier(MOD_ID, "example_block_two"),
                new ResourceExampleBlock()
        );
    }

    public static void registerEventListeners() {

        // Registers an event that checks if a block has been broken/left clicked
        GameEvents.AFTER_BLOCK_IS_BROKEN.register((zone, blockPosition, v) -> {
            LogWrapper.info("%s: Block has been broken at position (%s, %s, %s)"
                    .formatted(
                            createTag("BREAK_LISTENER"),
                            blockPosition.getGlobalX(),
                            blockPosition.getGlobalY(),
                            blockPosition.getGlobalZ()
                    )
            );
        });

        // Registers an event that checks if a block has been place
        GameEvents.AFTER_BLOCK_IS_PLACED.register((zone, blockState, blockPosition, v) -> {
            LogWrapper.info("%s: Block \"%s[%s]\" has been placed at position (%s, %s, %s)"
                    .formatted(
                            createTag("PLACE_LISTENER"),
                            blockState.getBlock().getStringId(),
                            blockState.stringId,
                            blockPosition.getGlobalX(),
                            blockPosition.getGlobalY(),
                            blockPosition.getGlobalZ()
                    )
            );
        });

    }

}
