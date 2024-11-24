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
import net.minecraft.client.resources.PlayerSkin;
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
	    		new HumanoidArmorLayer<>(this, new DummyPlayerArmorModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new DummyPlayerArmorModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()),
	    		new HumanoidArmorLayer<>(this, new DummyPlayerArmorModel(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)), new DummyPlayerArmorModel(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)), context.getModelManager())));
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		this.addLayer(new DummyCapeLayer(this));
	    this.addLayer(new DummyElytraLayer(this, context.getModelSet()));
	    this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
	}

	@Override
	public void render(DummyPlayerEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        this.model = switch (entity.clientData().skin().model()) {
            case WIDE -> normal;
			case SLIM -> slim;
        };
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected boolean shouldShowName(DummyPlayerEntity entity) {
		return entity.getProfile().name().isPresent() || entity.hasCustomName();
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
		return entity.clientData().skin().texture();
	}
}
