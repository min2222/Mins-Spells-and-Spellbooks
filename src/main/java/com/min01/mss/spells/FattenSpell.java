package com.min01.mss.spells;

import java.util.function.Predicate;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.misc.MSSSchools;
import com.min01.mss.misc.MSSTargetEntityCastData;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.root.PreventDismount;
import io.redspace.ironsspellbooks.network.casting.SyncTargetingDataPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.PartEntity;

public class FattenSpell extends AbstractSpell
{
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(MinsSpellbooks.MODID, "fatten");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(MSSSchools.TROLL_RESOURCE)
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
    
    //copied from Utils
    public static boolean preCastTargetHelper(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, int range, float aimAssist, boolean sendFailureMessage, Predicate<LivingEntity> filter) 
    {
        var target = Utils.raycastForEntity(caster.level, caster, range, true, aimAssist);
        LivingEntity livingTarget = null;
        if(target instanceof EntityHitResult entityHit)
        {
            if(entityHit.getEntity() instanceof LivingEntity livingEntity && filter.test(livingEntity)) 
            {
                livingTarget = livingEntity;
            }
            else if (entityHit.getEntity() instanceof PartEntity<?> partEntity && partEntity.getParent() instanceof LivingEntity livingParent && !caster.equals(livingParent) && filter.test(livingParent)) 
            {
            	livingTarget = livingParent;
            } 
            else if(entityHit.getEntity() instanceof PreventDismount) 
            {
                if(entityHit.getEntity().getFirstPassenger() instanceof LivingEntity livingRooted) 
                {
                    livingTarget = livingRooted;
                }
            }
        }

        if(livingTarget != null) 
        {
        	MSSTargetEntityCastData data = new MSSTargetEntityCastData();
        	data.setTarget(livingTarget);
            playerMagicData.setAdditionalCastData(data);
            if(caster instanceof ServerPlayer serverPlayer) 
            {
                if(spell.getCastType() != CastType.INSTANT)
                {
                    PacketDistributor.sendToPlayer(serverPlayer, new SyncTargetingDataPacket(livingTarget, spell));
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
        if(castData instanceof MSSTargetEntityCastData targetData)
        {
        	LivingEntity targetEntity = targetData.getTarget(level);
    		CompoundTag tag = targetEntity.getPersistentData();
    		float fattenRender = tag.getFloat("FattenRender");
    		float fattenSize = tag.getFloat("FattenSize");
    		if(entity.isShiftKeyDown())
    		{
            	tag.putFloat("FattenRender", Math.max(fattenRender - 1.0F, 0.0F));
            	tag.putFloat("FattenSize", Math.max(fattenSize - 1.0F, 1.0F));
    		}
    		else
    		{
            	tag.putFloat("FattenRender", Math.min(fattenRender + 1.0F, spellLevel));
            	tag.putFloat("FattenSize", Math.min(fattenSize + 1.0F, spellLevel));
    		}
        	targetEntity.refreshDimensions();
        }
    	
    	super.onClientCast(level, spellLevel, entity, castData);
    }
    
    @Override
    public ICastDataSerializable getEmptyCastData()
    {
    	return new MSSTargetEntityCastData();
    }
    
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) 
    {
        if(playerMagicData.getAdditionalCastData() instanceof MSSTargetEntityCastData targetData)
        {
            LivingEntity targetEntity = targetData.getTarget(level);
    		CompoundTag tag = targetEntity.getPersistentData();
    		float fattenSize = tag.getFloat("FattenSize");
    		fattenSize = Math.max(fattenSize, 1.0F);
    		if(entity.isShiftKeyDown())
    		{
            	tag.putFloat("FattenSize", Math.max(fattenSize - 1.0F, 0.0F));
            	tag.putBoolean("Shrink", true);
    		}
    		else
    		{
            	tag.putFloat("FattenSize", Math.min(fattenSize + 1.0F, spellLevel));
    		}
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
