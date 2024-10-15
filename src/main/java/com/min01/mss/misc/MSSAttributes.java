package com.min01.mss.misc;

import com.min01.mss.MinsSpellbooks;

import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSSAttributes
{
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MinsSpellbooks.MODID);
    
    public static final RegistryObject<Attribute> TROLL_MAGIC_RESIST = newResistanceAttribute("troll");
    public static final RegistryObject<Attribute> TROLL_SPELL_POWER = newPowerAttribute("troll");
    
    public static RegistryObject<Attribute> newResistanceAttribute(String id) 
    {
        return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicRangedAttribute("attribute.mins_spellbooks." + id + "_magic_resist", 1.0D, -100, 100).setSyncable(true)));
    }

    public static RegistryObject<Attribute> newPowerAttribute(String id)
    {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicRangedAttribute("attribute.mins_spellbooks." + id + "_spell_power", 1.0D, -100, 100).setSyncable(true)));
    }
}
