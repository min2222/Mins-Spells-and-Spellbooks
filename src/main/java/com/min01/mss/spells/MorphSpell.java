package com.min01.mss.spells;

import com.min01.morph.capabilities.MorphCapabilities;
import com.min01.mss.MinsSpellbooks;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.spells.eldritch.AbstractEldritchSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class MorphSpell extends AbstractEldritchSpell
{
    private final ResourceLocation spellId = new ResourceLocation(MinsSpellbooks.MODID, "morph");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(1000)
            .build();

    public MorphSpell() 
    {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 200;
        this.baseManaCost = 1000;
    }
    
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) 
    {
    	return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35F);
    }
    
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData)
    {
        if(playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData)
        {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);
            if(!(target instanceof Player))
            {
                entity.getCapability(MorphCapabilities.MORPH).ifPresent(t -> 
    			{
    				LivingEntity morph = (LivingEntity) target.getType().create(entity.level);
					t.setMorph(morph);
					entity.setHealth(morph.getMaxHealth());
    			});
            }
        }
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
