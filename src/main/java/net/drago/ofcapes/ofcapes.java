package net.drago.ofcapes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class ofcapes implements ModInitializer {
    private static boolean colytraIsLoaded;

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isModLoaded("colytra"))
            colytraIsLoaded = true;
    }

    public static boolean isColytraLoaded() {
        return colytraIsLoaded;
    }
}