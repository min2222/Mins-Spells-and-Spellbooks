package com.min01.mss.event;

import com.min01.mss.MinsSpellbooks;
import com.mojang.math.Axis;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinsSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{
	@SubscribeEvent
	public static void onLivingRender(RenderLivingEvent.Pre<?, ?> event)
	{
		LivingEntity entity = event.getEntity();
		CompoundTag tag = entity.getPersistentData();
		if(tag.contains("FattenRender"))
		{
			float scale = tag.getFloat("FattenRender");
			event.getPoseStack().scale(1.0F + scale, 1.0F, 1.0F + scale);
		}
		if(tag.contains("Spinning"))
		{
			event.getPoseStack().mulPose(Axis.YP.rotationDegrees((entity.tickCount + event.getPartialTick()) * -75.0F));
		}
	}
}
