package com.min01.mss.spells;

import java.util.List;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.misc.MSSSchools;
import com.min01.mss.network.MSSNetwork;
import com.min01.mss.network.UpdateSpinningTagPacket;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SpinningSpell extends AbstractSpell
{
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(MinsSpellbooks.MODID, "spinning");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MSSSchools.TROLL_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(30)
            .build();

    public SpinningSpell() 
    {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 10;
    }
    
    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData)
    {
		CompoundTag tag = entity.getPersistentData();
		tag.putBoolean("Spinning", true);
    	
    	super.onClientCast(level, spellLevel, entity, castData);
    }
    
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) 
    {
		CompoundTag tag = entity.getPersistentData();
		tag.putBoolean("Spinning", true);
		
    	super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
    
    @Override
    public void onServerCastComplete(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) 
    {
		CompoundTag tag = entity.getPersistentData();
    	if(entity instanceof ServerPlayer player)
    	{
        	MSSNetwork.sendNonLocal(new UpdateSpinningTagPacket(player.getUUID()), player);
    	}
		if(tag.contains("Spinning"))
		{
			tag.remove("Spinning");
		}
    	
    	super.onServerCastComplete(level, spellLevel, entity, playerMagicData, cancelled);
    }
    
    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData)
    {
    	super.onServerCastTick(level, spellLevel, entity, playerMagicData);
    	
    	List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1.0F), t -> t != entity && !t.isAlliedTo(entity));
    	list.forEach(t -> 
    	{
    		if(t.hurt(entity.damageSources().indirectMagic(entity, entity), spellLevel * 0.5F))
    		{
    			t.invulnerableTime = 0;
    		}
    	});
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
