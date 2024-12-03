package com.min01.mss.spells;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.entity.EntityLaserSegment;
import com.min01.mss.entity.MSSEntities;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

@AutoSpellConfig
public class BouncingLaserSpell extends AbstractSpell
{
    private final ResourceLocation spellId = new ResourceLocation(MinsSpellbooks.MODID, "bouncing_laser");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(30)
            .build();

    public BouncingLaserSpell() 
    {
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 30;
    }
    
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) 
    {
    	EntityLaserSegment segment = new EntityLaserSegment(MSSEntities.LASER_SEGMENT.get(), level);
    	HitResult result = Utils.raycastForEntity(level, entity, 100.0F, true, 0.1F);
    	segment.setPos(result.getLocation());
    	segment.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 5.0F, 0.0F);
    	level.addFreshEntity(segment);
    	
    	EntityLaserSegment segment1 = new EntityLaserSegment(MSSEntities.LASER_SEGMENT.get(), level);
    	segment1.setPos(entity.getEyePosition());
    	segment1.setOwner(segment);
    	level.addFreshEntity(segment1);
    	
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
		return CastType.INSTANT;
	}
}
