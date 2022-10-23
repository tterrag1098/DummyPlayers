package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class DummyPlayerEntityRenderer extends LivingEntityRenderer<DummyPlayerEntity, DummyPlayerModel> {

	private final DummyPlayerModel slim;
	private final DummyPlayerModel normal;
	
	public DummyPlayerEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new DummyPlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0);
		this.normal = this.model;
		this.slim = new DummyPlayerModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
		this.addLayer(new DummyStandLayer(this, context.getModelSet()));
	    this.addLayer(new DummyArmorLayer<>(this,
	    		new HumanoidArmorLayer<>(this, new DummyPlayerArmorModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new DummyPlayerArmorModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))),
	    		new HumanoidArmorLayer<>(this, new DummyPlayerArmorModel(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)), new DummyPlayerArmorModel(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)))));
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new DummyCapeLayer(this));
	    this.addLayer(new DummyElytraLayer(this, context.getModelSet()));
	    this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
	}

	@Override
	public void render(DummyPlayerEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if ("default".equals(entityIn.getSkinType())) {
			this.model = normal;
		} else {
			this.model = slim;
		}
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected boolean shouldShowName(DummyPlayerEntity entity) {
		return !entity.getProfile().equals(DummyPlayerEntity.NULL_PROFILE) || entity.hasCustomName();
	}

	@Override
	protected void scale(DummyPlayerEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
		if (!entitylivingbaseIn.isNoBasePlate()) {
			matrixStackIn.translate(0, -1f / 16f, 0);
		}
		float f = 0.9375F;
		matrixStackIn.scale(f, f, f);
	}

	@Override
	public ResourceLocation getTextureLocation(DummyPlayerEntity entity) {
		return entity.getSkin();
	}
}
