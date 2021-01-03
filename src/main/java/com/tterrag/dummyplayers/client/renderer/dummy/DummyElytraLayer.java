package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class DummyElytraLayer extends LayerRenderer<DummyPlayerEntity, DummyPlayerModel> {

	private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
	private final ElytraModel<DummyPlayerEntity> modelElytra = new ElytraModel<>();

	public DummyElytraLayer(IEntityRenderer<DummyPlayerEntity, DummyPlayerModel> rendererIn) {
		super(rendererIn);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DummyPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.CHEST);
		if (shouldRender(itemstack, entitylivingbaseIn)) {
			ResourceLocation resourcelocation = getElytraTexture(itemstack, entitylivingbaseIn);
			matrixStackIn.push();
			matrixStackIn.translate(0.0D, 0.0D, 0.125D);
			this.getEntityModel().copyModelAttributesTo(this.modelElytra);
			this.modelElytra.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(bufferIn, this.modelElytra.getRenderType(resourcelocation), false, itemstack.hasEffect());
			this.modelElytra.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			matrixStackIn.pop();
		}
	}

	private boolean shouldRender(ItemStack stack, DummyPlayerEntity entity) {
		return stack.getItem() == Items.ELYTRA;
	}

	private ResourceLocation getElytraTexture(ItemStack stack, DummyPlayerEntity entity) {
		return entity.getElytra() == null ? TEXTURE_ELYTRA : entity.getElytra();
	}
}