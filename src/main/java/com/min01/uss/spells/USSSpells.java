package com.min01.uss.spells;

import com.min01.uss.UselessSpellbooks;

import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class USSSpells
{
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, UselessSpellbooks.MODID);
    public static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, UselessSpellbooks.MODID);
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, UselessSpellbooks.MODID);

    public static final ResourceLocation TROLL_RESOURCE = new ResourceLocation(UselessSpellbooks.MODID, "troll");
    
    public static final RegistryObject<Attribute> TROLL_MAGIC_RESIST = newResistanceAttribute("troll");
    public static final RegistryObject<Attribute> TROLL_SPELL_POWER = newPowerAttribute("troll");
    public static final ResourceKey<DamageType> TROLL_MAGIC = registerDamageType("troll_magic");
    
    public static final RegistryObject<SchoolType> TROLL = registerSchool(new SchoolType(
            TROLL_RESOURCE,
            ModTags.HOLY_FOCUS,
            Component.translatable("school.useless_spellbooks.troll").withStyle(ChatFormatting.WHITE),
            LazyOptional.of(TROLL_SPELL_POWER::get),
            LazyOptional.of(TROLL_MAGIC_RESIST::get),
            LazyOptional.of(SoundRegistry.HOLY_CAST::get),
            TROLL_MAGIC));

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell)
    {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
    
    public static ResourceKey<DamageType> registerDamageType(String name) 
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(UselessSpellbooks.MODID, name));
    }
    
    public static RegistryObject<SchoolType> registerSchool(SchoolType schoolType)
    {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }
    
    public static RegistryObject<Attribute> newResistanceAttribute(String id) 
    {
        return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicRangedAttribute("attribute.useless_spellbooks." + id + "_magic_resist", 1.0D, -100, 100).setSyncable(true)));
    }

    public static RegistryObject<Attribute> newPowerAttribute(String id)
    {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicRangedAttribute("attribute.useless_spellbooks." + id + "_spell_power", 1.0D, -100, 100).setSyncable(true)));
    }
}
