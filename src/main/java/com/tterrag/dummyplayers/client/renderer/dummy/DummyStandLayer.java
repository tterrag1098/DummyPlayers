package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tterrag.dummyplayers.DummyPlayers;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class DummyStandLayer extends RenderLayer<DummyPlayerEntity, DummyPlayerModel> {
	
	public static final ModelLayerLocation LAYER = new ModelLayerLocation(DummyPlayers.DUMMY_PLAYER.getId(), "stand");

	private final HierarchicalModel<DummyPlayerEntity> standModel;
	private final ModelPart standBase;

    public DummyStandLayer(RenderLayerParent<DummyPlayerEntity, DummyPlayerModel> entityRendererIn, EntityModelSet context) {
        super(entityRendererIn);
        this.standModel = new HierarchicalModel<>(RenderType::entitySolid) {
            @Override
            public void setupAnim(DummyPlayerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            }

            @Override
            public ModelPart root() {
                return standBase;
            }
        };
        
        this.standBase = context.bakeLayer(LAYER);
    }
    
    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("base_plate", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, 11.0F, -6.0F, 12.0F, 1.0F, 12.0F), PartPose.offset(0.0F, 12.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
     }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, DummyPlayerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    	if (entity.isNoBasePlate()) return;
        this.standBase.yRot = Mth.DEG_TO_RAD * -Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        poseStack.pushPose();
        poseStack.translate(0, 1f / 16f, 0);
    	this.standModel.renderToBuffer(poseStack, bufferSource.getBuffer(this.standModel.renderType(ArmorStandRenderer.DEFAULT_SKIN_LOCATION)), packedLight, OverlayTexture.NO_OVERLAY);
    	poseStack.popPose();
    }
}
