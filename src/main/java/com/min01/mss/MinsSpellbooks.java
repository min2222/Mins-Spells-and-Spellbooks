package com.min01.mss;

import com.min01.mss.entity.MSSEntities;
import com.min01.mss.misc.MSSAttributes;
import com.min01.mss.misc.MSSSchools;
import com.min01.mss.network.MSSNetwork;
import com.min01.mss.spells.MSSSpells;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinsSpellbooks.MODID)
public class MinsSpellbooks 
{
	public static final String MODID = "mins_spellbooks";
	
	public MinsSpellbooks() 
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MSSSpells.SPELLS.register(bus);
		MSSSchools.SCHOOLS.register(bus);
		MSSAttributes.ATTRIBUTES.register(bus);
		MSSEntities.ENTITY_TYPES.register(bus);
		
		MSSNetwork.registerMessages();
	}
}
