package com.tmtravlr.potioncore;

import net.minecraftforge.common.config.Configuration;

import com.tmtravlr.potioncore.PotionCoreEffects.PotionData;

public class ConfigLoader {

	public static Configuration config;
	
	public static boolean fixInvisibility;
	public static boolean fixBlindness;
	
	public static void load() {
		config.load();
		
		fixInvisibility = config.getBoolean("Fix Invisibiliby", "_options", true, "Fixes Invisibiliby so mobs can't see you as close while you are invisible.\nThey can see you at 1-12 blocks away depending on how much armor you\nhave on and if you are holding a item or not.");
		fixBlindness = config.getBoolean("Fix Blindness", "_options", true, "Fixes Blindness so mobs can't see things to attack unless they are really\nclose.");
		
		for(String name : PotionCoreEffects.potionMap.keySet()) {
			PotionData data = PotionCoreEffects.potionMap.get(name);
			
			data.enabled = config.getBoolean("Enabled", name, data.enabled, "Is the " + name + " potion enabled?");
			//data.id = config.getInt("Id", name, data.id, 0, 255, "Id of the " + name + " potion.");
		}
		
		config.save();
	}
	
}
