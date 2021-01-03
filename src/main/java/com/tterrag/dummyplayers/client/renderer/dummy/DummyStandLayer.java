package com.tterrag.dummyplayers.client.renderer.dummy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tterrag.dummyplayers.entity.DummyPlayerEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.MathHelper;

public class DummyStandLayer extends LayerRenderer<DummyPlayerEntity, DummyPlayerModel> {

	private final Model standModel;
	private final ModelRenderer standBase;

    public DummyStandLayer(IEntityRenderer<DummyPlayerEntity, DummyPlayerModel> entityRendererIn) {
        super(entityRendererIn);
        this.standModel = new Model(RenderType::getEntitySolid) {
        	
        	@Override
        	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        		standBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        	}
        };
        this.standModel.textureHeight = this.standModel.textureWidth = 64;
		this.standBase = new ModelRenderer(standModel, 0, 32);
		this.standBase.addBox(-6.0F, 11.0F, -6.0F, 12.0F, 1.0F, 12.0F, 0);
		this.standBase.setRotationPoint(0.0F, 12.0F, 0.0F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DummyPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    	if (entityIn.hasNoBasePlate()) return;
        this.standBase.rotateAngleY = ((float)Math.PI / 180F) * -MathHelper.interpolateAngle(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw);
        matrixStackIn.push();
        matrixStackIn.translate(0, 1f / 16f, 0);
    	this.standModel.render(matrixStackIn, bufferIn.getBuffer(this.standModel.getRenderType(ArmorStandRenderer.TEXTURE_ARMOR_STAND)), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    	matrixStackIn.pop();
    }
}