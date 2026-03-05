package com.min01.mss.effect;

import com.min01.mss.MinsSpellbooks;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSSEffects 
{
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MinsSpellbooks.MODID);
	
	public static final RegistryObject<MobEffect> DODGE = EFFECTS.register("dodge", () -> new DodgeEffect());
}
