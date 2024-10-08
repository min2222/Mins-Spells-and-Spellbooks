package com.min01.uss;

import com.min01.uss.spells.USSSpells;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UselessSpellbooks.MODID)
public class UselessSpellbooks 
{
	public static final String MODID = "useless_spellbooks";
	
	public UselessSpellbooks() 
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		USSSpells.SPELLS.register(bus);
		USSSpells.SCHOOLS.register(bus);
		USSSpells.ATTRIBUTES.register(bus);
	}
}
