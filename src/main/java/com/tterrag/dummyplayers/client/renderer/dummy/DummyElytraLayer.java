package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;

public class DummyElytraLayer extends RenderLayer<DummyPlayerEntity, DummyPlayerModel> {

	private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
	private final ElytraModel<DummyPlayerEntity> modelElytra;

	public DummyElytraLayer(RenderLayerParent<DummyPlayerEntity, DummyPlayerModel> rendererIn, EntityModelSet models) {
		super(rendererIn);
	    this.modelElytra = new ElytraModel<>(models.bakeLayer(ModelLayers.ELYTRA));
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DummyPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack itemstack = entitylivingbaseIn.getItemBySlot(EquipmentSlot.CHEST);
		if (shouldRender(itemstack, entitylivingbaseIn)) {
			ResourceLocation resourcelocation = getElytraTexture(itemstack, entitylivingbaseIn);
			matrixStackIn.pushPose();
			matrixStackIn.translate(0.0D, 0.0D, 0.125D);
			this.getParentModel().copyPropertiesTo(this.modelElytra);
			this.modelElytra.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, this.modelElytra.renderType(resourcelocation), false, itemstack.hasFoil());
			this.modelElytra.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			matrixStackIn.popPose();
		}
	}

	private boolean shouldRender(ItemStack stack, DummyPlayerEntity entity) {
		return stack.getItem() == Items.ELYTRA;
	}

	private ResourceLocation getElytraTexture(ItemStack stack, DummyPlayerEntity entity) {
		return entity.getElytra() == null ? TEXTURE_ELYTRA : entity.getElytra();
	}
}