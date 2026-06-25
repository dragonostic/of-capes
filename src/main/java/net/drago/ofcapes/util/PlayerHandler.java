package net.drago.ofcapes.util;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public class PlayerHandler {
    public static Map<String, Identifier> capes = new HashMap<>();

    public interface ReturnCapeTexture {
        void response(Identifier id);
    }

    public static void loadPlayerCape(GameProfile player, ReturnCapeTexture response) {
        try {
            String uuid = player.id().toString();
            URL capeURL = new URI(String.format("http://s.optifine.net/capes/%s.png", player.name())).toURL();
            CompletableFuture.supplyAsync(() -> {
                try (InputStream capeStream = capeURL.openStream()) {
                    return parseCape(NativeImage.read(capeStream));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).thenAcceptAsync(capeImage -> {
                Identifier capeTexture = Identifier.fromNamespaceAndPath("of-capes", uuid);

                DynamicTexture nIBT = new DynamicTexture(() -> "of-capes:" + uuid, capeImage);
                Minecraft.getInstance().getTextureManager().register(capeTexture, nIBT);

                capes.put(uuid, capeTexture);
                response.response(capeTexture);
            }, Minecraft.getInstance());
        } catch (Exception ignored) {}
    }

    public static NativeImage parseCape(NativeImage image) {
        if(image == null) return null;

        int imageWidth = 64;
        int imageHeight = 32;
        int imageSrcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        for (int imageSrcHeight = image.getHeight(); imageWidth < imageSrcWidth
                || imageHeight < imageSrcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < imageSrcWidth; x++) {
            for (int y = 0; y < srcHeight; y++) {
                imgNew.setPixel(x, y, image.getPixel(x, y));
            }
        }
        image.close();
        return imgNew;
    }
}
