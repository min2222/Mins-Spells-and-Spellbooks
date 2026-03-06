package com.min01.mss.entity;

import java.util.Optional;
import java.util.UUID;

import com.min01.mss.misc.Laser;
import com.min01.mss.misc.Laser.LaserHitResult;
import com.min01.mss.misc.MSSEntityDataSerializers;
import com.min01.mss.util.MSSUtil;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityLaserSegment extends Projectile
{
	public static final EntityDataAccessor<Integer> BOUNCE = SynchedEntityData.defineId(EntityLaserSegment.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> SPELL_LEVEL = SynchedEntityData.defineId(EntityLaserSegment.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(EntityLaserSegment.class, EntityDataSerializers.OPTIONAL_UUID);
	public static final EntityDataAccessor<Vec3> TARGET_POS = SynchedEntityData.defineId(EntityLaserSegment.class, MSSEntityDataSerializers.VEC3.get());
	public static final int MAX_BOUNCE = 10;
	
	public final Laser laser = new Laser();
	
	public EntityLaserSegment(EntityType<? extends Projectile> pEntityType, Level pLevel) 
	{
		super(pEntityType, pLevel);
		this.noCulling = true;
		this.setNoGravity(true);
	}

	public EntityLaserSegment(EntityType<? extends Projectile> pEntityType, double pX, double pY, double pZ, Level pLevel) 
	{
		this(pEntityType, pLevel);
		this.setPos(pX, pY, pZ);
	}

	public EntityLaserSegment(EntityType<? extends Projectile> pEntityType, LivingEntity pShooter, Level pLevel) 
	{
		this(pEntityType, pShooter.getX(), pShooter.getEyeY() - (double)0.1F, pShooter.getZ(), pLevel);
		this.setOwner(pShooter);
	}

	@Override
	protected void defineSynchedData() 
	{
		this.entityData.define(BOUNCE, 0);
		this.entityData.define(SPELL_LEVEL, 0);
		this.entityData.define(OWNER_UUID, Optional.empty());
		this.entityData.define(TARGET_POS, Vec3.ZERO);
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double pDistance)
	{
		return true;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
    	if(!this.getTargetPos().equals(Vec3.ZERO) && this.tickCount == 1 && this.getOwner() != null)
    	{
            LaserHitResult laserHit = this.laser.raytrace(this.level, this.position(), this.getTargetPos(), 0.5F, t -> t != this.getOwner() && !t.isAlliedTo(this.getOwner()), this);
            laserHit.entities.forEach(t -> 
            {
        		if(t.hurt(this.damageSources().indirectMagic(this.getOwner(), this), 5.0F + (this.getBounce() * this.getSpellLevel())))
        		{
        			t.invulnerableTime = 0;
        		}
            });
    	}
	    
		if(this.tickCount >= 10)
		{
			this.discard();
		}
	}
	
	@Override
	public void onHitBlock(BlockHitResult pResult) 
	{
		super.onHitBlock(pResult);
		
	    int bounce = this.getBounce();
	    Vec3 movement = this.getTargetPos().subtract(this.position());
	    double x = movement.x;
	    double y = movement.y;
	    double z = movement.z;

		Direction direction = pResult.getDirection();
		
		if(direction == Direction.EAST) 
		{
			x = -x * 0.5F;
		}
		else if(direction == Direction.SOUTH) 
		{
			z = -z * 0.5F;
		}
		else if(direction == Direction.WEST) 
		{
			x = -x * 0.5F;
		}
		else if(direction == Direction.NORTH)
		{
			z = -z * 0.5F;
		}
		else if(direction == Direction.UP)
		{
			y = -y * 0.5F;
		}
		else if(direction == Direction.DOWN)
		{
			y = -y * 0.5F;
		}
		
		if(bounce < MAX_BOUNCE * this.getSpellLevel())
		{
			Vec3 motion = new Vec3(x, y, z).normalize().scale(100.0F);
			
			EntityLaserSegment segment = new EntityLaserSegment(MSSEntities.LASER_SEGMENT.get(), this.level);
			segment.setPos(this.getTargetPos());
			segment.setOwner(this.getOwner());
	    	segment.setSpellLevel(this.getSpellLevel());
			segment.setBounce(bounce + 1);
			
			Laser laser = new Laser();
			LaserHitResult laserHit = laser.raytrace(segment.level, segment.position(), motion.add(segment.position()), 0.5F, t -> t != segment.getOwner() && !t.isAlliedTo(segment.getOwner()), segment);
			segment.setTargetPos(laser.collidePos);
			this.level.addFreshEntity(segment);
			
			if(laserHit.blockHit != null)
			{
	    		segment.onHitBlock(laserHit.blockHit);
			}
		}
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound)
	{
		super.readAdditionalSaveData(pCompound);
		this.setBounce(pCompound.getInt("Bounce"));
		this.setSpellLevel(pCompound.getInt("SpellLevel"));
		CompoundTag nbt = pCompound.getCompound("TargetPos");
		this.setTargetPos(new Vec3(nbt.getDouble("X"), nbt.getDouble("Y"), nbt.getDouble("Z")));
		if(pCompound.hasUUID("Owner")) 
		{
			this.entityData.set(OWNER_UUID, Optional.of(pCompound.getUUID("Owner")));
		}
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound)
	{
		super.addAdditionalSaveData(pCompound);
		pCompound.putInt("Bounce", this.getBounce());
		pCompound.putInt("SpellLevel", this.getSpellLevel());
		CompoundTag nbt = new CompoundTag();
		nbt.putDouble("X", this.getTargetPos().x);
		nbt.putDouble("Y", this.getTargetPos().y);
		nbt.putDouble("Z", this.getTargetPos().z);
		pCompound.put("TargetPos", nbt);
		if(this.entityData.get(OWNER_UUID).isPresent())
		{
			pCompound.putUUID("Owner", this.entityData.get(OWNER_UUID).get());
		}
	}
	
	@Override
	public void setOwner(Entity pOwner)
	{
		if(pOwner == null)
		{
			this.entityData.set(OWNER_UUID, Optional.empty());
		}
		else
		{
			this.entityData.set(OWNER_UUID, Optional.of(pOwner.getUUID()));
		}
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
	
	public void setBounce(int value)
	{
		this.entityData.set(BOUNCE, value);
	}
	
	public int getBounce()
	{
		return this.entityData.get(BOUNCE);
	}
	
	public void setSpellLevel(int value)
	{
		this.entityData.set(SPELL_LEVEL, value);
	}
	
	public int getSpellLevel()
	{
		return this.entityData.get(SPELL_LEVEL);
	}
	
	public void setTargetPos(Vec3 pos)
	{
		this.entityData.set(TARGET_POS, pos);
	}
	
	public Vec3 getTargetPos()
	{
		return this.entityData.get(TARGET_POS);
	}
}
