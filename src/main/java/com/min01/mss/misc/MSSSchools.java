package com.min01.mss.misc;

import com.min01.mss.MinsSpellbooks;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MSSSchools 
{
    public static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, MinsSpellbooks.MODID);

    public static final ResourceLocation EXTRA_SCHOOL = ResourceLocation.fromNamespaceAndPath(MinsSpellbooks.MODID, "extra");
    
    public static final RegistryObject<SchoolType> EXTRA = registerSchool(new SchoolType(
    		EXTRA_SCHOOL,
            MSSTags.EXTRA_FOCUS,
            Component.translatable("school.mins_spellbooks.extra").withStyle(ChatFormatting.WHITE),
            () -> MSSAttributes.EXTRA_SPELL_POWER.get(),
            () -> MSSAttributes.EXTRA_MAGIC_RESIST.get(),
            () -> SoundRegistry.HOLY_CAST.get(),
            MSSDamageTypes.EXTRA_MAGIC));
    
    public static RegistryObject<SchoolType> registerSchool(SchoolType schoolType)
    {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }
}
