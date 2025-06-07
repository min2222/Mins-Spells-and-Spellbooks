package com.min01.mss.mixin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.min01.mss.config.MSSConfig;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.weapons.StaffOfTheNines;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@Mixin(StaffOfTheNines.class)
public class MixinStaffOfTheNines
{
	@Redirect(at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/util/Utils;raycastForEntity(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;FZF)Lnet/minecraft/world/phys/HitResult;"), method = "use", remap = false)
	public HitResult use(Level level, Entity originEntity, float distance, boolean checkForBlocks, float bbInflation)
	{
		if(MSSConfig.enableStaffOfTheNinesShieldBypass.get())
		{
			return raycastForEntity(level, originEntity, distance, checkForBlocks, bbInflation);
		}
		else
		{
			return Utils.raycastForEntity(level, originEntity, distance, checkForBlocks, bbInflation);
		}
	}
	
    private static HitResult raycastForEntity(Level level, Entity originEntity, float distance, boolean checkForBlocks, float bbInflation)
    {
        Vec3 start = originEntity.getEyePosition();
        Vec3 end = originEntity.getLookAngle().normalize().scale(distance).add(start);

        return internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, bbInflation, t -> t.isPickable() && t.isAlive());
    }
    
    private static HitResult internalRaycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks, float bbInflation, Predicate<? super Entity> filter) 
    {
        BlockHitResult blockHitResult = null;
        if(checkForBlocks) 
        {
            blockHitResult = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, originEntity));
            end = blockHitResult.getLocation();
        }
        AABB range = originEntity.getBoundingBox().expandTowards(end.subtract(start));

        List<HitResult> hits = new ArrayList<>();
        List<? extends Entity> entities = level.getEntities(originEntity, range, filter);
        for(Entity target : entities) 
        {
            HitResult hit = checkEntityIntersecting(target, start, end, bbInflation);
            if(hit.getType() != HitResult.Type.MISS)
            {
            	hits.add(hit);
            }
        }

        if(!hits.isEmpty())
        {
            hits.sort(Comparator.comparingDouble(o -> o.getLocation().distanceToSqr(start)));
            return hits.get(0);
        } 
        else if(checkForBlocks) 
        {
            return blockHitResult;
        }
        return BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end));
    }
    
    private static HitResult checkEntityIntersecting(Entity entity, Vec3 start, Vec3 end, float bbInflation)
    {
        Vec3 hitPos = null;
        if(entity.getType() != EntityRegistry.SHIELD_ENTITY.get() && !entity.isMultipartEntity())
        {
            hitPos = entity.getBoundingBox().inflate(bbInflation).clip(start, end).orElse(null);
        }
        if(hitPos != null)
        {
            return new EntityHitResult(entity, hitPos);
        }
        else
        {
            return BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end));
        }
    }
}
