package com.tterrag.dummyplayers.client.renderer.dummy;

import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.item.ArmorStandEntity;

public class DummyPlayerModel extends PlayerModel<DummyPlayerEntity> {

	public DummyPlayerModel(float modelSize, boolean smallArmsIn) {
		super(modelSize, smallArmsIn);
	}

	static void setArmorStandAngles(ArmorStandEntity entityIn, BipedModel<? extends ArmorStandEntity> model) {
		model.bipedHead.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getHeadRotation().getX();
		model.bipedHead.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getHeadRotation().getY();
		model.bipedHead.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getHeadRotation().getZ();
//		model.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
		model.bipedBody.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getBodyRotation().getX();
		model.bipedBody.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getBodyRotation().getY();
		model.bipedBody.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getBodyRotation().getZ();
		model.bipedLeftArm.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getLeftArmRotation().getX();
		model.bipedLeftArm.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getLeftArmRotation().getY();
		model.bipedLeftArm.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getLeftArmRotation().getZ();
		model.bipedRightArm.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getRightArmRotation().getX();
		model.bipedRightArm.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getRightArmRotation().getY();
		model.bipedRightArm.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getRightArmRotation().getZ();
		model.bipedLeftLeg.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getLeftLegRotation().getX();
		model.bipedLeftLeg.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getLeftLegRotation().getY();
		model.bipedLeftLeg.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getLeftLegRotation().getZ();
//		model.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
		model.bipedRightLeg.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getRightLegRotation().getX();
		model.bipedRightLeg.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getRightLegRotation().getY();
		model.bipedRightLeg.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getRightLegRotation().getZ();
//		model.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
		model.bipedHeadwear.copyModelAngles(model.bipedHead);
	}

	@Override
	public void setRotationAngles(DummyPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		setArmorStandAngles(entityIn, this);
		this.bipedLeftArm.showModel = true;
		this.bipedRightArm.showModel = true;
		this.bipedBodyWear.copyModelAngles(this.bipedBody);
		this.bipedLeftArmwear.copyModelAngles(this.bipedLeftArm);
		this.bipedRightArmwear.copyModelAngles(this.bipedRightArm);
		this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);
		this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
	}
}
