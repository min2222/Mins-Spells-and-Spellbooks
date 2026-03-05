package com.min01.mss.spells;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.entity.EntityLaserSegment;
import com.min01.mss.entity.MSSEntities;
import com.min01.mss.misc.Laser;
import com.min01.mss.misc.Laser.LaserHitResult;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BouncingLaserSpell extends AbstractSpell
{
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(MinsSpellbooks.MODID, "bouncing_laser");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(5)
            .build();

    public BouncingLaserSpell() 
    {
        this.baseManaCost = 30;
        this.manaCostPerLevel = 10;
    }
    
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) 
    {
		EntityLaserSegment segment = new EntityLaserSegment(MSSEntities.LASER_SEGMENT.get(), level);
        Vec3 start = entity.getEyePosition().subtract(0.0F, 0.1F, 0.0F);
        Vec3 end = entity.getLookAngle().normalize().scale(100.0F).add(start);
        Laser laser = new Laser();
		LaserHitResult laserHit = laser.raytrace(level, start, end, 0.5F, t -> t != entity && !t.isAlliedTo(entity), entity);
    	segment.setPos(start);
    	segment.setOwner(entity);
    	segment.setSpellLevel(spellLevel);
    	segment.setTargetPos(laser.collidePos);
    	level.addFreshEntity(segment);
    	if(laserHit.blockHit != null)
    	{
    		segment.onHitBlock(laserHit.blockHit);
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
		return CastType.INSTANT;
	}
}
