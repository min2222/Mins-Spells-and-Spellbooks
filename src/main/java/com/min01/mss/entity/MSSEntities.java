package com.min01.mss.entity;

import com.min01.mss.MinsSpellbooks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSSEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MinsSpellbooks.MODID);

	public static final RegistryObject<EntityType<EntityLaserSegment>> LASER_SEGMENT = registerEntity("laser_segment", EntityType.Builder.<EntityLaserSegment>of(EntityLaserSegment::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F));
	
	public static <T extends Entity> EntityType.Builder<T> createBuilder(EntityType.EntityFactory<T> factory, MobCategory category)
	{
		return EntityType.Builder.<T>of(factory, category);
	}
	
	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) 
	{
		return ENTITY_TYPES.register(name, () -> builder.build(new ResourceLocation(MinsSpellbooks.MODID, name).toString()));
	}
}
