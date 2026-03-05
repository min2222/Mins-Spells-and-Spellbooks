package com.min01.mss.spells;

import com.min01.mss.MinsSpellbooks;
import com.min01.mss.misc.MSSSchools;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DisorganizationSpell extends AbstractSpell
{
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(MinsSpellbooks.MODID, "disorganization");
    
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(MSSSchools.EXTRA_SCHOOL)
            .setMaxLevel(1)
            .setCooldownSeconds(120)
            .build();

    public DisorganizationSpell() 
    {
        this.baseManaCost = 200;
        this.castTime = 200;
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
    	Inventory inventory = player.getInventory();
        int inventoryHalf = inventory.getContainerSize() / 2;
        for(int i = 0; i < inventoryHalf; i++)
        {
            int slot = level.random.nextInt(inventoryHalf);
            ItemStack swapStack = inventory.getItem(i);
            inventory.setItem(i, inventory.getItem(inventoryHalf + slot));
            inventory.setItem(inventoryHalf + slot, swapStack);
        }
        inventory.setChanged();
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
