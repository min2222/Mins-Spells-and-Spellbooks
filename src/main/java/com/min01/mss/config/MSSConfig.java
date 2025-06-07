package com.min01.mss.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class MSSConfig 
{
	public static final MSSConfig CONFIG;
	public static final ForgeConfigSpec CONFIG_SPEC;

	public static ForgeConfigSpec.BooleanValue enableStaffOfTheNinesShieldBypass;
	
    static 
    {
    	Pair<MSSConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(MSSConfig::new);
    	CONFIG = pair.getLeft();
    	CONFIG_SPEC = pair.getRight();
    }
	
    public MSSConfig(ForgeConfigSpec.Builder config) 
    {
    	config.push("Settings");
    	MSSConfig.enableStaffOfTheNinesShieldBypass = config.comment("disable/enable shield spell bypass for staff of the nines").define("enableStaffOfTheNinesShieldBypass", true);
        config.pop();
    }
}
