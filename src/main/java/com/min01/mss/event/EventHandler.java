package com.min01.mss.event;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.spells.MSSSpells;

import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinsSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler
{
    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event)
    {
        event.getTypes().forEach(entity -> 
        {
            event.add(entity, MSSSpells.TROLL_MAGIC_RESIST.get());
            event.add(entity, MSSSpells.TROLL_SPELL_POWER.get());
        });
    }
}
