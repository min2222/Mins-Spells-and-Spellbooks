package com.min01.uss.misc;

import java.util.UUID;

import javax.annotation.Nullable;

import com.min01.uss.util.USSUtil;

import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class USSTargetEntityCastData implements ICastDataSerializable
{
    private UUID targetUUID;
    
    public void setTarget(LivingEntity entity)
    {
    	this.targetUUID = entity.getUUID();
    }
    
    @Nullable
    public LivingEntity getTarget(Level level)
    {
        return (LivingEntity) USSUtil.getEntityByUUID(level, this.targetUUID);
    }
    
	@Override
	public void reset() 
	{
		
	}

	@Override
	public void writeToBuffer(FriendlyByteBuf buffer)
	{
		buffer.writeUUID(this.targetUUID);
	}

	@Override
	public void readFromBuffer(FriendlyByteBuf buffer)
	{
		this.targetUUID = buffer.readUUID();
	}

	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		tag.putUUID("TargetUUID", this.targetUUID);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.targetUUID = nbt.getUUID("TargetUUID");
	}
}
