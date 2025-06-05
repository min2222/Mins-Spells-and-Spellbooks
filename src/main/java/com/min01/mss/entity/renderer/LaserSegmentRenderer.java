package com.min01.mss.entity.renderer;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.entity.EntityLaserSegment;
import com.min01.mss.util.MSSClientUtil;
import com.min01.mss.util.MSSUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class LaserSegmentRenderer extends EntityRenderer<EntityLaserSegment>
{
    public static final ResourceLocation WHITE_TEXTURE = new ResourceLocation(MinsSpellbooks.MODID, "textures/misc/white.png");
    
	public LaserSegmentRenderer(Context pContext)
	{
		super(pContext);
	}
	
	@Override
	public void render(EntityLaserSegment pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight)
	{
		if(!pEntity.getTargetPos().equals(Vec3.ZERO))
		{
			pPoseStack.pushPose();
			Vec3 size = new Vec3(0.1F / 2, 0.1F / 2, pEntity.position().distanceTo(pEntity.getTargetPos()));
			AABB aabb = new AABB(size.reverse(), new Vec3(0.1F / 2, 0.1F / 2, 0.0F));
			Vec2 rot = MSSUtil.lookAt(pEntity.position(), pEntity.getTargetPos());
			pPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
			pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F + rot.y));
			pPoseStack.mulPose(Axis.XP.rotationDegrees(rot.x));
			MSSClientUtil.drawBox(aabb, pPoseStack, pBuffer, Vec3.fromRGB24(4924869), LightTexture.FULL_BLOCK, 255, RenderType.eyes(WHITE_TEXTURE));
			pPoseStack.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityLaserSegment pEntity)
	{
		return null;
	}
}
