package com.min01.mss.spells;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.effect.MSSEffects;
import com.min01.mss.misc.MSSSchools;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class DodgeSpell extends AbstractSpell
{
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(MinsSpellbooks.MODID, "dodge");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MSSSchools.EXTRA_SCHOOL)
            .setMaxLevel(3)
            .setCooldownSeconds(60)
            .build();

    public DodgeSpell()
    {
        this.baseManaCost = 150;
        this.manaCostPerLevel = 50;
        this.castTime = 60;
    }
    
    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) 
    {
    	int duration = spellLevel * 10;
    	entity.addEffect(new MobEffectInstance(MSSEffects.DODGE.get(), duration * 20, spellLevel - 1));
    	super.onClientCast(level, spellLevel, entity, castData);
    }
    
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) 
    {
    	int duration = spellLevel * 10;
    	entity.addEffect(new MobEffectInstance(MSSEffects.DODGE.get(), duration * 20, spellLevel - 1));
    	super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
    
	@Override
	public ResourceLocation getSpellResource() 
	{
		return this.spellId;
	}

	@Override
	public DefaultConfig getDefaultConfig() 
	{
		return this.defaultConfig;
	}

	@Override
	public CastType getCastType() 
	{
		return CastType.LONG;
	}
}
