package com.tterrag.dummyplayers.client.renderer.dummy;

import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public class DummyPlayerArmorModel extends HumanoidModel<DummyPlayerEntity> {

	public DummyPlayerArmorModel(ModelPart pRoot) {
		super(pRoot);
	}

	@Override
	public void setupAnim(DummyPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		DummyPlayerModel.setArmorStandAngles(entityIn, this);
	}
}
