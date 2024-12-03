package com.min01.mss.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import com.min01.mss.util.MSSUtil;

import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityLaserSegment extends Projectile
{
	public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(EntityLaserSegment.class, EntityDataSerializers.OPTIONAL_UUID);
	public static final String BOUNCE = "Bounce";
	public static final int MAX_BOUNCE = 10;
	
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
		this.entityData.define(OWNER_UUID, Optional.empty());
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double pDistance)
	{
		double d0 = this.getBoundingBox().getSize() * 4.0D;
		if(Double.isNaN(d0))
		{
			d0 = 4.0D;
		}

		d0 *= 64.0D;
		return pDistance < d0 * d0;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		this.checkInsideBlocks();
		this.updateRotation();
		HitResult hitResult = getHitResultOnMoveVector(this, this::canHitEntity);
		if(hitResult.getType() != HitResult.Type.MISS && this.getDeltaMovement() != Vec3.ZERO)
		{
			this.onHit(hitResult);
		}
		
    	if(this.getOwner() != null && this.tickCount == 1)
    	{
    		List<LivingEntity> arrayList = new ArrayList<>();
            Vec3 vec3 = this.position();
            Vec3 vec31 = this.getOwner().position().subtract(vec3);
            Vec3 vec32 = vec31.normalize();

            for(int i = 1; i < Mth.floor(vec31.length()) + this.distanceTo(this.getOwner()); ++i)
            {
            	Vec3 vec33 = vec3.add(vec32.scale(i));
            	List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(vec33, vec33).inflate(0.1F));
            	list.removeIf(t -> t == this.getOwner() || t.isAlliedTo(this.getOwner()));
            	list.forEach(t -> 
            	{
            		if(!arrayList.contains(t))
            		{
            			arrayList.add(t);
            		}
            	});
            }
            
            arrayList.forEach(t -> 
            {
        		if(t.hurt(this.damageSources().magic(), this.getPersistentData().getInt(BOUNCE) + 1.0F))
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
	
	public static HitResult getHitResultOnMoveVector(EntityLaserSegment pProjectile, Predicate<Entity> pFilter) 
	{
		Vec3 vec3 = pProjectile.getDeltaMovement();
		Level level = pProjectile.level();
		Vec3 vec31 = pProjectile.position();
		return getHitResult(vec31, pProjectile, pFilter, vec3, level);
	}
	
	private static HitResult getHitResult(Vec3 pStartVec, Entity pProjectile, Predicate<Entity> pFilter, Vec3 pEndVecOffset, Level pLevel) 
	{
		Vec3 vec3 = pStartVec.add(pEndVecOffset);
		HitResult hitresult = pLevel.clip(new ClipContext(pStartVec, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pProjectile));
		if(hitresult.getType() != HitResult.Type.MISS) 
		{
			vec3 = hitresult.getLocation();
		}

		HitResult hitresult1 = ProjectileUtil.getEntityHitResult(pLevel, pProjectile, pStartVec, vec3, pProjectile.getBoundingBox().expandTowards(pEndVecOffset).inflate(1.0D), pFilter);
		if(hitresult1 != null)
		{
			hitresult = hitresult1;
		}
		return hitresult;
	}
	
	@Override
	protected void onHitBlock(BlockHitResult pResult) 
	{
		super.onHitBlock(pResult);
		
	    int bounce = this.getPersistentData().getInt(BOUNCE);
	    double x = this.getDeltaMovement().x;
	    double y = this.getDeltaMovement().y;
	    double z = this.getDeltaMovement().z;

		Direction direction = pResult.getDirection();
		
		if(direction == Direction.EAST) 
		{
			x = -x;
		}
		else if(direction == Direction.SOUTH) 
		{
			z = -z;
		}
		else if(direction == Direction.WEST) 
		{
			x = -x;
		}
		else if(direction == Direction.NORTH)
		{
			z = -z;
		}
		else if(direction == Direction.UP)
		{
			y = -y;
		}
		else if(direction == Direction.DOWN)
		{
			y = -y;
		}
		
		if(bounce < MAX_BOUNCE)
		{
			EntityLaserSegment segment = new EntityLaserSegment(MSSEntities.LASER_SEGMENT.get(), this.level);
			Vec3 motion = new Vec3(x, y, z);
			Vec3 vec3 = this.collide(motion);
			segment.setPos(pResult.getLocation().add(vec3));
			segment.setDeltaMovement(motion);
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
