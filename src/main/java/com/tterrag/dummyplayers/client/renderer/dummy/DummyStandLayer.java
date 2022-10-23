package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

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

public class DummyStandLayer extends RenderLayer<DummyPlayerEntity, DummyPlayerModel> {
	
	public static final ModelLayerLocation LAYER = new ModelLayerLocation(DummyPlayerEntity.DUMMY_PLAYER.getId(), "stand");

	private final Model standModel;
	private final ModelPart standBase;

    public DummyStandLayer(RenderLayerParent<DummyPlayerEntity, DummyPlayerModel> entityRendererIn, EntityModelSet context) {
        super(entityRendererIn);
        this.standModel = new Model(RenderType::entitySolid) {
        	
        	@Override
        	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        		standBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
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
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DummyPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    	if (entityIn.isNoBasePlate()) return;
        this.standBase.yRot = ((float)Math.PI / 180F) * -Mth.rotLerp(partialTicks, entityIn.yRotO, entityIn.getYRot());
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 1f / 16f, 0);
    	this.standModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(this.standModel.renderType(ArmorStandRenderer.DEFAULT_SKIN_LOCATION)), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    	matrixStackIn.popPose();
    }
}