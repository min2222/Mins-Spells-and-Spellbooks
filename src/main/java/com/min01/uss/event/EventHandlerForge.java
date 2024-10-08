package com.min01.uss.event;

import com.min01.uss.UselessSpellbooks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UselessSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge
{
    @SuppressWarnings("removal")
	@SubscribeEvent
    public static void onEntitySize(EntityEvent.Size event)
    {
    	Entity entity = event.getEntity();
    	if(entity.getPersistentData().contains("Fatten"))
    	{
    		event.setNewSize(EntityDimensions.fixed(entity.getBbWidth() * 2.0F, entity.getBbHeight()));
    		entity.getPersistentData().remove("Fatten");
    	}
    }
}
