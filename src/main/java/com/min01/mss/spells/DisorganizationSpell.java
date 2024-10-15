package com.min01.mss.spells;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.misc.MSSSchools;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class DisorganizationSpell extends AbstractSpell
{
    private final ResourceLocation spellId = new ResourceLocation(MinsSpellbooks.MODID, "disorganization");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(MSSSchools.TROLL_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(30)
            .build();

    public DisorganizationSpell() 
    {
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 30;
    }
    
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData)
    {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 6, 0.1F);
    }
    
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) 
    {
        if(playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData)
        {
            LivingEntity targetEntity = targetData.getTarget((ServerLevel) level);
            if(targetEntity instanceof Player player)
            {
            	this.disorganization(level, player);
            }
        }
        else if(entity instanceof Player player)
        {
        	this.disorganization(level, player);
        }
        
    	super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
    
    public void disorganization(Level level, Player player)
    {
        int inventoryHalf = player.getInventory().items.size() / 2;
        for(int i = 0; i < inventoryHalf; i++)
        {
            int slot = level.random.nextInt(inventoryHalf);
            ItemStack swapStack = player.getInventory().items.get(i);
            player.getInventory().items.set(i, player.getInventory().items.get(inventoryHalf + slot));
            player.getInventory().items.set(inventoryHalf + slot, swapStack);
        }
        int slot2 = level.random.nextInt(player.getInventory().items.size());
        ItemStack swapStack2 = player.getInventory().items.get(slot2);
        player.getInventory().items.set(slot2, player.getOffhandItem());
        player.setItemInHand(InteractionHand.OFF_HAND, swapStack2);
        player.getInventory().setChanged();
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
