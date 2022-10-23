package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.mojang.math.Vector3f;

public class DummyCapeLayer extends RenderLayer<DummyPlayerEntity, DummyPlayerModel> {

	public DummyCapeLayer(RenderLayerParent<DummyPlayerEntity, DummyPlayerModel> playerModelIn) {
		super(playerModelIn);
	}

	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DummyPlayerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!entity.isInvisible() && entity.getCape() != null) {
			ItemStack itemstack = entity.getItemBySlot(EquipmentSlot.CHEST);
			if (itemstack.getItem() != Items.ELYTRA) {
				matrixStackIn.pushPose();
				matrixStackIn.translate(0.0D, 0.0D, 0.125D);

	            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(6.0F));
	            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
				VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(entity.getCape()));
				this.getParentModel().renderCloak(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY);
				matrixStackIn.popPose();
			}
		}
	}
}