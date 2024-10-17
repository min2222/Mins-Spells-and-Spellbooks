package com.min01.mss.event;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.entity.MSSEntities;
import com.min01.mss.entity.renderer.LaserSegmentRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinsSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler 
{
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    	event.registerEntityRenderer(MSSEntities.LASER_SEGMENT.get(), LaserSegmentRenderer::new);
    }
}
