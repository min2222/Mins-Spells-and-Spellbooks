package com.min01.mss;

import com.min01.mss.config.MSSConfig;
import com.min01.mss.entity.MSSEntities;
import com.min01.mss.misc.MSSAttributes;
import com.min01.mss.misc.MSSEntityDataSerializers;
import com.min01.mss.misc.MSSSchools;
import com.min01.mss.network.MSSNetwork;
import com.min01.mss.spells.MSSSpells;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinsSpellbooks.MODID)
public class MinsSpellbooks 
{
	public static final String MODID = "mins_spellbooks";
	
	public MinsSpellbooks(FMLJavaModLoadingContext ctx) 
	{
		IEventBus bus = ctx.getModEventBus();
		MSSSpells.SPELLS.register(bus);
		MSSSchools.SCHOOLS.register(bus);
		MSSAttributes.ATTRIBUTES.register(bus);
		MSSEntities.ENTITY_TYPES.register(bus);
		MSSEntityDataSerializers.SERIALIZERS.register(bus);
		
		MSSNetwork.registerMessages();
		ctx.registerConfig(Type.COMMON, MSSConfig.CONFIG_SPEC, "mins-spellbooks.toml");
	}
}
