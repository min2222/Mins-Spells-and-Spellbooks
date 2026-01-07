package com.min01.mss.misc;

import com.min01.mss.MinsSpellbooks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class MSSDamageTypes
{
    public static final ResourceKey<DamageType> TROLL_MAGIC = registerDamageType("troll_magic");
    
    public static ResourceKey<DamageType> registerDamageType(String name) 
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MinsSpellbooks.MODID, name));
    }
}
