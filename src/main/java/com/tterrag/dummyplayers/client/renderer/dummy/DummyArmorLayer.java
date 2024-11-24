package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class DummyArmorLayer<T extends DummyPlayerEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
	private final HumanoidArmorLayer<T, M, A> normal, slim;
	
	public DummyArmorLayer(RenderLayerParent<T, M> pRenderer, HumanoidArmorLayer<T, M, A> normal, HumanoidArmorLayer<T, M, A> slim) {
		super(pRenderer);
		this.normal = normal;
		this.slim = slim;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		HumanoidArmorLayer<T, M, A> model = switch (entity.clientData().skin().model()) {
			case WIDE -> normal;
			case SLIM -> slim;
		};
		model.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);;
	}
}
