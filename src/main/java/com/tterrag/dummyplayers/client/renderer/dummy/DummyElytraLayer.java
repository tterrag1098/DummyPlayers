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
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class DummyElytraLayer extends RenderLayer<DummyPlayerEntity, DummyPlayerModel> {
	private static final ResourceLocation TEXTURE_ELYTRA = ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");

	private final ElytraModel<DummyPlayerEntity> model;

	public DummyElytraLayer(RenderLayerParent<DummyPlayerEntity, DummyPlayerModel> parent, EntityModelSet models) {
		super(parent);
	    this.model = new ElytraModel<>(models.bakeLayer(ModelLayers.ELYTRA));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, DummyPlayerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack chestItem = entity.getItemBySlot(EquipmentSlot.CHEST);
		if (shouldRender(chestItem)) {
			ResourceLocation texture = getElytraTexture(entity);
			poseStack.pushPose();
			poseStack.translate(0.0D, 0.0D, 0.125D);
			this.getParentModel().copyPropertiesTo(this.model);
			this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			VertexConsumer builder = ItemRenderer.getFoilBuffer(bufferSource, this.model.renderType(texture), false, chestItem.hasFoil());
			this.model.renderToBuffer(poseStack, builder, packedLight, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}
	}

	private boolean shouldRender(ItemStack stack) {
		return stack.is(Items.ELYTRA);
	}

	private ResourceLocation getElytraTexture(DummyPlayerEntity entity) {
		PlayerSkin skin = entity.clientData().skin();
		if (skin.elytraTexture() != null) {
			return skin.elytraTexture();
		} else if (skin.capeTexture() != null) {
			return skin.capeTexture();
		}
		return TEXTURE_ELYTRA;
	}
}
