package com.min01.mss.effect;

import java.util.List;

import org.joml.Math;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class DodgeEffect extends BasicMSSEffect
{
	public DodgeEffect() 
	{
		super(MobEffectCategory.BENEFICIAL, 16777215);
	}
	
	@Override
	public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
	{
		int amplifier = pAmplifier + 1;
		List<Projectile> list = pLivingEntity.level.getEntitiesOfClass(Projectile.class, pLivingEntity.getBoundingBox().inflate(Math.min(amplifier * 1.5F, 4.0F)), t -> this.canDodge(t, pLivingEntity));
		list.forEach(t -> 
		{
			this.tryDodge(t, pLivingEntity, 16, 24, 1.5F);
		});
	}
	
	public boolean canDodge(Projectile projectile, LivingEntity living)
	{
		if(projectile.getPersistentData().contains("Dodged"))
		{
			return false;
		}
		double x = projectile.getX() - projectile.xo;
		double z = projectile.getZ() - projectile.zo;
		if(x * x + z * z > (double)2.5000003E-7F)
		{
			if(projectile.getOwner() != null)
			{
				return !projectile.getOwner().isAlliedTo(living);
			}
			return true;
		}
		return false;
	}
	
    public void tryDodge(Entity projectile, LivingEntity entity, float rangeVertical, float rangeHorizontal, float velocity)
    {
    	float width = projectile.getBbWidth() + 0.5F;
        Vec3 motion = projectile.getDeltaMovement();
        Vec3 position = entity.position();
        double vH = motion.horizontalDistance();
        Vec3 direction = new Vec3(motion.x / vH, motion.y / vH, motion.z / vH);
        int distanceY = Math.abs((int) position.y - (int) projectile.position().y);
        if(distanceY <= rangeVertical)
        {
            double distanceX = position.x - projectile.position().x;
            double distanceZ = position.z - projectile.position().z;
            double distanceH = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);
            if(distanceH <= rangeHorizontal)
            {
                double cos = (direction.x * distanceX + direction.z * distanceZ) / distanceH;
                double sin = Math.sqrt(1 - cos * cos);
                if(width >= distanceH * sin)
                {
                    Vec3 selfAxis = new Vec3(0.0, 1.0, 0.0);
                    Vec3 dodgeDirection = selfAxis.cross(direction).scale(velocity);
                    entity.setDeltaMovement(dodgeDirection.x, 0, dodgeDirection.z);
                    entity.hurtMarked = true;
                    projectile.getPersistentData().putBoolean("Dodged", true);
                }
            }
        }
    }
}
