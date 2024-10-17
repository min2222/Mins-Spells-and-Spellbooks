package com.min01.mss.spells;

import com.min01.mss.MinsSpellbooks;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MSSSpells
{
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, MinsSpellbooks.MODID);

    //Troll
    public static final RegistryObject<AbstractSpell> DISORGANIZATION = registerSpell(new DisorganizationSpell());
    public static final RegistryObject<AbstractSpell> FATTEN = registerSpell(new FattenSpell());
    public static final RegistryObject<AbstractSpell> SPINNING = registerSpell(new SpinningSpell());
    
    public static final RegistryObject<AbstractSpell> BOUNCING_LASER = registerSpell(new BouncingLaserSpell());

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell)
    {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
}
