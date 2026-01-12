package com.min01.mss.event;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.util.MSSUtil;
import com.min01.tickrateapi.util.TickrateUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinsSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge
{
    @SuppressWarnings("removal")
	@SubscribeEvent
    public static void onEntitySize(EntityEvent.Size event)
    {
    	Entity entity = event.getEntity();
    	if(entity.getPersistentData().contains("FattenSize"))
    	{
    		float size = entity.getPersistentData().getFloat("FattenSize");
    		if(entity.getPersistentData().getBoolean("Shrink"))
    		{
        		event.setNewSize(EntityDimensions.fixed(entity.getBbWidth() - size, entity.getBbHeight()));
    		}
    		else
    		{
        		event.setNewSize(EntityDimensions.fixed(entity.getBbWidth() + size, entity.getBbHeight()));
    		}
    		entity.getPersistentData().remove("FattenSize");
    		entity.getPersistentData().remove("Shrink");
    	}
    }
    
	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent event)
	{
		Iterable<Entity> all = MSSUtil.getAllEntities(event.level);
		for(Entity entity : all)
		{
			if(!entity.getPersistentData().contains("ForceTickCount"))
			{
				continue;
			}
			int time = entity.getPersistentData().getInt("ForceTickCount");
			if(time <= 0)
			{
				TickrateUtil.resetTickrate(entity);
				entity.getPersistentData().remove("ForceTickCount");
			}
			else
			{
				entity.getPersistentData().putInt("ForceTickCount", time - 1);
			}
		}
	}
}
