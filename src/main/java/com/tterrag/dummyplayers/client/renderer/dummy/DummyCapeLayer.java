package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3f;

public class DummyCapeLayer extends LayerRenderer<DummyPlayerEntity, DummyPlayerModel> {

	public DummyCapeLayer(IEntityRenderer<DummyPlayerEntity, DummyPlayerModel> playerModelIn) {
		super(playerModelIn);
	}

	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DummyPlayerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!entity.isInvisible() && entity.getCape() != null) {
			ItemStack itemstack = entity.getItemStackFromSlot(EquipmentSlotType.CHEST);
			if (itemstack.getItem() != Items.ELYTRA) {
				matrixStackIn.push();
				matrixStackIn.translate(0.0D, 0.0D, 0.125D);

	            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(6.0F));
	            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
				IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(entity.getCape()));
				this.getEntityModel().renderCape(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY);
				matrixStackIn.pop();
			}
		}
	}
}