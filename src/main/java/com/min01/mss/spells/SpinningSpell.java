package com.min01.mss.spells;

import com.min01.mss.MinsSpellbooks;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class SpinningSpell extends AbstractSpell
{
    private final ResourceLocation spellId = new ResourceLocation(MinsSpellbooks.MODID, "spinning");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MSSSpells.TROLL_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(30)
            .build();

    public SpinningSpell() 
    {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 20;
    }
    
    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData)
    {
		//FIXME remove tag when spell end
		CompoundTag tag = entity.getPersistentData();
		tag.putBoolean("Spinning", true);
    	
    	super.onClientCast(level, spellLevel, entity, castData);
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
		return CastType.CONTINUOUS;
	}
}
