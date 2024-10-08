package com.min01.uss.event;

import com.min01.uss.UselessSpellbooks;
import com.min01.uss.spells.USSSpells;

import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UselessSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler
{
    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event)
    {
        event.getTypes().forEach(entity -> 
        {
            event.add(entity, USSSpells.TROLL_MAGIC_RESIST.get());
            event.add(entity, USSSpells.TROLL_SPELL_POWER.get());
        });
    }
}
