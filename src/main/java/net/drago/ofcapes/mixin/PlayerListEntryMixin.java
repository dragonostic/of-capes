package net.drago.ofcapes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.drago.ofcapes.util.PlayerHandler;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PlayerInfo.class)
public class PlayerListEntryMixin {
    @Shadow
    @Final
    private GameProfile profile;
    @Unique
    private boolean loadedCapeTexture = false;
    @Unique
    private Identifier identifier;

    @ModifyReturnValue(method = "getSkin", at = @At("TAIL"))
    protected PlayerSkin changeCapeTexture(PlayerSkin original) {
        if(!loadedCapeTexture) fetchCapeTexture();

        ClientAsset.Texture newTexture = new ClientAsset.Texture() {
            @Override
            public Identifier texturePath() {
                return identifier;
            }

            @Override
            public Identifier id() {
                return identifier;
            }
        };

        return new PlayerSkin(
                original.body(),
                identifier == null ? original.cape() : newTexture,
                identifier == null ? original.elytra() : newTexture,
                original.model(),
                original.secure()
        );
    }

    @Unique
    private void fetchCapeTexture() {
        loadedCapeTexture = true;
        PlayerHandler.loadPlayerCape(this.profile, id -> this.identifier = id);
    }
}