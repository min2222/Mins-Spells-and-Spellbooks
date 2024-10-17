package com.min01.mss.entity;

import java.util.Optional;
import java.util.UUID;

import com.min01.mss.util.MSSUtil;

import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityLaserSegment extends ThrowableProjectile
{
	public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(EntityLaserSegment.class, EntityDataSerializers.OPTIONAL_UUID);
	public static final String BOUNCE = "Bounce";
	public static final int MAX_BOUNCE = 10;
	
	public EntityLaserSegment(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.setNoGravity(true);
		this.noCulling = true;
	}
	
	public EntityLaserSegment(EntityType<? extends ThrowableProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel)
	{
		super(pEntityType, pX, pY, pZ, pLevel);
		this.setNoGravity(true);
		this.noCulling = true;
	}

	public EntityLaserSegment(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel) 
	{
		super(pEntityType, pShooter, pLevel);
		this.setNoGravity(true);
		this.noCulling = true;
	}

	@Override
	protected void defineSynchedData() 
	{
		this.entityData.define(OWNER_UUID, Optional.empty());
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		
		if(this.tickCount >= 10)
		{
			this.discard();
		}
	}
	
	@Override
	protected void onHitBlock(BlockHitResult pResult) 
	{
		super.onHitBlock(pResult);
		
	    int bounce = this.getPersistentData().getInt(BOUNCE);
	    double motionx = this.getDeltaMovement().x;
	    double motiony = this.getDeltaMovement().y;
	    double motionz = this.getDeltaMovement().z;

		Direction direction = pResult.getDirection();
		
		if(direction == Direction.EAST) 
		{
			motionx = -motionx;
		}
		else if(direction == Direction.SOUTH) 
		{
			motionz = -motionz;
		}
		else if(direction == Direction.WEST) 
		{
			motionx = -motionx;
		}
		else if(direction == Direction.NORTH)
		{
			motionz = -motionz;
		}
		else if(direction == Direction.UP)
		{
			motiony = -motiony;
		}
		else if(direction == Direction.DOWN)
		{
			motiony = -motiony;
		}
		
		if(bounce < MAX_BOUNCE)
		{
			EntityLaserSegment segment = new EntityLaserSegment(MSSEntities.LASER_SEGMENT.get(), this.level);
			segment.setPos(pResult.getLocation());
			segment.setDeltaMovement(motionx, motiony, motionz);
			segment.getPersistentData().putInt(BOUNCE, bounce + 1);
			this.level.addFreshEntity(segment);
			this.setOwner(segment);
			this.setDeltaMovement(Vec3.ZERO);
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult pResult) 
	{
		
	}
	
	@Override
	public void setOwner(Entity pOwner)
	{
		this.entityData.set(OWNER_UUID, Optional.of(pOwner.getUUID()));
	}
	
	@Override
	public Entity getOwner() 
	{
		if(this.entityData.get(OWNER_UUID).isPresent())
		{
			return MSSUtil.getEntityByUUID(this.level, this.entityData.get(OWNER_UUID).get());
		}
		return null;
	}
}
