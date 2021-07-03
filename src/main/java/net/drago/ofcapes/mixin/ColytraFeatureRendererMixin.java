package net.drago.ofcapes.mixin;

import net.drago.ofcapes.util.PlayerHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.colytra.client.ColytraFeatureRenderer;

@Pseudo
@Environment(value = EnvType.CLIENT)
@Mixin(value = ColytraFeatureRenderer.class, remap = false)
public class ColytraFeatureRendererMixin<T extends LivingEntity> {
    private static final Identifier ELYTRA_SKIN = new Identifier("textures/entity/elytra.png");
    private T livingEntity;

    @Inject(method = "render", at = @At("HEAD"))
    public void captureLivingEntity(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        this.livingEntity = livingEntity;
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 0)
    private Identifier editTextureIdentifier(Identifier texture) {
        if (texture.equals(ELYTRA_SKIN) && livingEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity && PlayerHandler.fromPlayer(abstractClientPlayerEntity) != null && abstractClientPlayerEntity.isPartVisible(PlayerModelPart.CAPE))
            texture = PlayerHandler.fromPlayer(abstractClientPlayerEntity);
        return texture;
    }
}
