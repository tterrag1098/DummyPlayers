package com.tterrag.dummyplayers.client.renderer.dummy;

import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.decoration.ArmorStand;

public class DummyPlayerModel extends PlayerModel<DummyPlayerEntity> {

	public DummyPlayerModel(ModelPart pRoot, boolean pSlim) {
		super(pRoot, pSlim);
	}

	static void setArmorStandAngles(ArmorStand entityIn, HumanoidModel<? extends ArmorStand> model) {
		model.head.xRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getX();
		model.head.yRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getY();
		model.head.zRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getZ();
//		model.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
		model.body.xRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getX();
		model.body.yRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getY();
		model.body.zRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getZ();
		model.leftArm.xRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getX();
		model.leftArm.yRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getY();
		model.leftArm.zRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getZ();
		model.rightArm.xRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getX();
		model.rightArm.yRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getY();
		model.rightArm.zRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getZ();
		model.leftLeg.xRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getX();
		model.leftLeg.yRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getY();
		model.leftLeg.zRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getZ();
//		model.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
		model.rightLeg.xRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getX();
		model.rightLeg.yRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getY();
		model.rightLeg.zRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getZ();
//		model.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
		model.hat.copyFrom(model.head);
	}

	@Override
	public void setupAnim(DummyPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		setArmorStandAngles(entityIn, this);
		this.leftArm.visible = true;
		this.rightArm.visible = true;
		this.jacket.copyFrom(this.body);
		this.leftSleeve.copyFrom(this.leftArm);
		this.rightSleeve.copyFrom(this.rightArm);
		this.leftPants.copyFrom(this.leftLeg);
		this.rightPants.copyFrom(this.rightLeg);
	}
}
