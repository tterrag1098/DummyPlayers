package com.tterrag.dummyplayers.client.renderer.dummy;

import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.renderer.entity.model.BipedModel;

public class DummyPlayerArmorModel extends BipedModel<DummyPlayerEntity> {

	public DummyPlayerArmorModel(float modelSize) {
		this(modelSize, 64, 32);
	}

	protected DummyPlayerArmorModel(float modelSize, int textureWidthIn, int textureHeightIn) {
		super(modelSize, 0.0F, textureWidthIn, textureHeightIn);
	}

	@Override
	public void setRotationAngles(DummyPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		DummyPlayerModel.setArmorStandAngles(entityIn, this);
	}
}
