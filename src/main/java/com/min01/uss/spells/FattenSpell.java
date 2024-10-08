package com.min01.uss.spells;

import java.util.function.Predicate;

import com.min01.uss.UselessSpellbooks;
import com.min01.uss.misc.USSTargetEntityCastData;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.network.spell.ClientboundSyncTargetingData;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

@AutoSpellConfig
public class FattenSpell extends AbstractSpell
{
    private final ResourceLocation spellId = new ResourceLocation(UselessSpellbooks.MODID, "fatten");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(USSSpells.TROLL_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(10)
            .build();

    public FattenSpell() 
    {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 10;
        this.baseManaCost = 20;
    }
    
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData)
    {
        return preCastTargetHelper(level, entity, playerMagicData, this, 10, 0.1F, true, t -> true);
    }
    
    //copied from Utils;
    public static boolean preCastTargetHelper(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, int range, float aimAssist, boolean sendFailureMessage, Predicate<LivingEntity> filter)
    {
    	HitResult target = Utils.raycastForEntity(caster.level, caster, range, true, aimAssist);
    	if(target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget && filter.test(livingTarget)) 
    	{
    		USSTargetEntityCastData data = new USSTargetEntityCastData();
    		data.setTarget(livingTarget);
    		playerMagicData.setAdditionalCastData(data);
    		if(caster instanceof ServerPlayer serverPlayer) 
    		{
    			if(spell.getCastType() != CastType.INSTANT) 
    			{
    				Messages.sendToPlayer(new ClientboundSyncTargetingData(livingTarget, spell), serverPlayer);
    			}
    			serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success", livingTarget.getDisplayName().getString(), spell.getDisplayName(serverPlayer)).withStyle(ChatFormatting.GREEN)));
    		}
    		if(livingTarget instanceof ServerPlayer serverPlayer) 
    		{
                Utils.sendTargetedNotification(serverPlayer, caster, spell);
            }
            return true;
        }
    	else if(sendFailureMessage && caster instanceof ServerPlayer serverPlayer)
    	{
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.cast_error_target").withStyle(ChatFormatting.RED)));
        }
        return false;
    }
    
    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData)
    {
        if(castData instanceof USSTargetEntityCastData targetData)
        {
        	LivingEntity targetEntity = targetData.getTarget(level);
    		CompoundTag tag = targetEntity.getPersistentData();
    		tag.putBoolean("Fatten", true);
        	tag.putFloat("FattenRender", tag.getFloat("FattenRender") + 1.0F);
        	targetEntity.refreshDimensions();
        }
    	
    	super.onClientCast(level, spellLevel, entity, castData);
    }
    
    @Override
    public ICastDataSerializable getEmptyCastData()
    {
    	return new USSTargetEntityCastData();
    }
    
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) 
    {
        if(playerMagicData.getAdditionalCastData() instanceof USSTargetEntityCastData targetData)
        {
            LivingEntity targetEntity = targetData.getTarget(level);
            targetEntity.getPersistentData().putBoolean("Fatten", true);
            targetEntity.refreshDimensions();
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
