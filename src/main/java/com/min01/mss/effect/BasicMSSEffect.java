package com.min01.mss.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BasicMSSEffect extends MobEffect
{
	public BasicMSSEffect(MobEffectCategory pCategory, int pColor) 
	{
		super(pCategory, pColor);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) 
	{
		return duration > 0;
	}
}
