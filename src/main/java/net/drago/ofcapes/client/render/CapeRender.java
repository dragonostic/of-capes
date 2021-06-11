package net.drago.ofcapes.client.render;

import net.drago.ofcapes.util.PlayerHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class CapeRender
        extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public CapeRender(
            final FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(final MatrixStack matrixStack, final VertexConsumerProvider vertexConsumerProvider, final int i, final AbstractClientPlayerEntity abstractClientPlayerEntity, final float f, final float g, final float h, final float j, final float k, final float l) {
        if (abstractClientPlayerEntity.canRenderCapeTexture() && !abstractClientPlayerEntity.isInvisible() && abstractClientPlayerEntity.isPartVisible(PlayerModelPart.CAPE) && PlayerHandler.fromPlayer(abstractClientPlayerEntity) != null) {
            final ItemStack itemStack = abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack.getItem() != Items.ELYTRA) {

                matrixStack.push();
                matrixStack.translate(0.0D, 0.0D, 0.125D);
                final double d = MathHelper.lerp(h, abstractClientPlayerEntity.prevCapeX, abstractClientPlayerEntity.capeX) - MathHelper.lerp(h, abstractClientPlayerEntity.prevX, abstractClientPlayerEntity.getX());
                final double e = MathHelper.lerp(h, abstractClientPlayerEntity.prevCapeY, abstractClientPlayerEntity.capeY) - MathHelper.lerp(h, abstractClientPlayerEntity.prevY, abstractClientPlayerEntity.getY());
                final double m = MathHelper.lerp(h, abstractClientPlayerEntity.prevCapeZ, abstractClientPlayerEntity.capeZ) - MathHelper.lerp(h, abstractClientPlayerEntity.prevZ, abstractClientPlayerEntity.getZ());
                final float n = abstractClientPlayerEntity.prevBodyYaw + (abstractClientPlayerEntity.bodyYaw - abstractClientPlayerEntity.prevBodyYaw);
                final double o = MathHelper.sin(n * 0.017453292F);
                final double p = -MathHelper.cos(n * 0.017453292F);
                float q = (float) e * 10.0F;
                q = MathHelper.clamp(q, -6.0F, 32.0F);
                float r = (float) (d * o + m * p) * 100.0F;
                r = MathHelper.clamp(r, 0.0F, 150.0F);
                float s = (float) (d * p - m * o) * 100.0F;
                s = MathHelper.clamp(s, -20.0F, 20.0F);
                if (r < 0.0F) {
                    r = 0.0F;
                }

                final float t = MathHelper.lerp(h, abstractClientPlayerEntity.prevStrideDistance, abstractClientPlayerEntity.strideDistance);
                q += MathHelper.sin(MathHelper.lerp(h, abstractClientPlayerEntity.prevHorizontalSpeed, abstractClientPlayerEntity.horizontalSpeed) * 6.0F) * 32.0F * t;
                if (abstractClientPlayerEntity.isInSneakingPose()) {
                    q += 25.0F;
                }

                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0F + r / 2.0F + q));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(s / 2.0F));
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - s / 2.0F));
                final VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(PlayerHandler.fromPlayer(abstractClientPlayerEntity)));
                (this.getContextModel()).renderCape(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
                matrixStack.pop();
            }
        }
    }
}