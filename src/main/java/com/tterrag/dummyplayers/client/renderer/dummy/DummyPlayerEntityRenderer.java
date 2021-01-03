package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class DummyPlayerEntityRenderer extends LivingRenderer<DummyPlayerEntity, DummyPlayerModel> {

	private static final DummyPlayerModel SLIM = new DummyPlayerModel(0, true);
	private static final DummyPlayerModel DEFAULT = new DummyPlayerModel(0, false);
	
	public DummyPlayerEntityRenderer(EntityRendererManager rendererManager) {
		super(rendererManager, DEFAULT, 0);
		this.addLayer(new DummyStandLayer(this));
		this.addLayer(new BipedArmorLayer<>(this, new DummyPlayerArmorModel(0.5F), new DummyPlayerArmorModel(1.0F)));
		this.addLayer(new HeldItemLayer<>(this));
		this.addLayer(new DummyCapeLayer(this));
		this.addLayer(new HeadLayer<>(this));
		this.addLayer(new DummyElytraLayer(this));
	}

	@Override
	public void render(DummyPlayerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if ("default".equals(entityIn.getSkinType())) {
			this.entityModel = DEFAULT;
		} else {
			this.entityModel = SLIM;
		}
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected boolean canRenderName(DummyPlayerEntity entity) {
		return entity.getProfile() != null || entity.hasCustomName();
	}

	@Override
	protected void preRenderCallback(DummyPlayerEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		if (!entitylivingbaseIn.hasNoBasePlate()) {
			matrixStackIn.translate(0, -1f / 16f, 0);
		}
		float f = 0.9375F;
		matrixStackIn.scale(f, f, f);
	}

	@Override
	public ResourceLocation getEntityTexture(DummyPlayerEntity entity) {
		return entity.getSkin();
	}
}
