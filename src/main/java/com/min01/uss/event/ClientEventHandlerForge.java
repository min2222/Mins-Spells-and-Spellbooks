package com.min01.uss.event;

import com.min01.uss.UselessSpellbooks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UselessSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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
	}
}
