package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DummyCapeLayer extends RenderLayer<DummyPlayerEntity, DummyPlayerModel> {

	public DummyCapeLayer(RenderLayerParent<DummyPlayerEntity, DummyPlayerModel> playerModelIn) {
		super(playerModelIn);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, DummyPlayerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ResourceLocation capeTexture = entity.clientData().skin().capeTexture();
        if (entity.isInvisible() || capeTexture == null) {
            return;
        }
        ItemStack chestItemStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (chestItemStack.getItem() == Items.ELYTRA) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.125D);
        poseStack.mulPose(Axis.XP.rotationDegrees(6.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        this.getParentModel().renderCloak(poseStack, bufferSource.getBuffer(RenderType.entitySolid(capeTexture)), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
