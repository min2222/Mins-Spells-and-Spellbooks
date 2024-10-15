package com.min01.mss.misc;

import com.min01.mss.MinsSpellbooks;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MSSSchools 
{
    public static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, MinsSpellbooks.MODID);

    public static final ResourceLocation TROLL_RESOURCE = new ResourceLocation(MinsSpellbooks.MODID, "troll");
    
    public static final RegistryObject<SchoolType> TROLL = registerSchool(new SchoolType(
            TROLL_RESOURCE,
            MSSTags.TROLL_FOCUS,
            Component.translatable("school.mins_spellbooks.troll").withStyle(ChatFormatting.WHITE),
            LazyOptional.of(MSSAttributes.TROLL_SPELL_POWER::get),
            LazyOptional.of(MSSAttributes.TROLL_MAGIC_RESIST::get),
            LazyOptional.of(SoundRegistry.HOLY_CAST::get),
            MSSDamageTypes.TROLL_MAGIC));
    
    public static RegistryObject<SchoolType> registerSchool(SchoolType schoolType)
    {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }
}
