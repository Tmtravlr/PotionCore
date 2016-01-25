package com.tmtravlr.potioncore;

import java.util.HashMap;

import net.minecraftforge.fml.common.FMLLog;

import com.tmtravlr.potioncore.effects.*;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

public class PotionCoreEffects {
	
	//Data about each potion
	public static class PotionData {
		public boolean enabled;
		//public int id;
		public PotionCorePotion potion = null;
		public Class<? extends PotionCorePotion> potionClass;
		
		public PotionData(/*int idToSet, */Class<? extends PotionCorePotion> classToSet) {
			//id = idToSet;
			potionClass = classToSet;
			enabled = true;
		}
	}

	public static HashMap<String, PotionData> potionMap = new HashMap<String, PotionData>();
	
	//Load the default potion options
	static {
		//int idCount = 40;
		potionMap.put(PotionFire.NAME, new PotionData(/*idCount++, */PotionFire.class));
		potionMap.put(PotionLightning.NAME, new PotionData(/*idCount++, */PotionLightning.class));
		potionMap.put(PotionExplosion.NAME, new PotionData(/*idCount++, */PotionExplosion.class));
		potionMap.put(PotionWeight.NAME, new PotionData(/*idCount++, */PotionWeight.class));
		potionMap.put(PotionRecoil.NAME, new PotionData(/*idCount++, */PotionRecoil.class));
		potionMap.put(PotionDrown.NAME, new PotionData(/*idCount++, */PotionDrown.class));
		potionMap.put(PotionArchery.NAME, new PotionData(/*idCount++, */PotionArchery.class));
		potionMap.put(PotionKlutz.NAME, new PotionData(/*idCount++, */PotionKlutz.class));
		potionMap.put(PotionVulnerable.NAME, new PotionData(/*idCount++, */PotionVulnerable.class));
		potionMap.put(PotionAntidote.NAME, new PotionData(/*idCount++, */PotionAntidote.class));
		potionMap.put(PotionPurity.NAME, new PotionData(/*idCount++, */PotionPurity.class));
		potionMap.put(PotionCure.NAME, new PotionData(/*idCount++, */PotionCure.class));
		potionMap.put(PotionDispel.NAME, new PotionData(/*idCount++, */PotionDispel.class));
		potionMap.put(PotionLevitate.NAME, new PotionData(/*idCount++, */PotionLevitate.class));
		potionMap.put(PotionSlowfall.NAME, new PotionData(/*idCount++, */PotionSlowfall.class));
		potionMap.put(PotionSolidCore.NAME, new PotionData(/*idCount++, */PotionSolidCore.class));
		potionMap.put(PotionSpin.NAME, new PotionData(/*idCount++, */PotionSpin.class));
		potionMap.put(PotionLaunch.NAME, new PotionData(/*idCount++, */PotionLaunch.class));
		potionMap.put(PotionClimb.NAME, new PotionData(/*idCount++, */PotionClimb.class));
		potionMap.put(PotionLove.NAME, new PotionData(/*idCount++, */PotionLove.class));
		potionMap.put(PotionStepup.NAME, new PotionData(/*idCount++, */PotionStepup.class));
		potionMap.put(PotionPerplexity.NAME, new PotionData(/*idCount++, */PotionPerplexity.class));
		potionMap.put(PotionDisorganization.NAME, new PotionData(/*idCount++, */PotionDisorganization.class));
		potionMap.put(PotionRepair.NAME, new PotionData(/*idCount++, */PotionRepair.class));
		potionMap.put(PotionRust.NAME, new PotionData(/*idCount++, */PotionRust.class));
		potionMap.put(PotionExtension.NAME, new PotionData(/*idCount++, */PotionExtension.class));
		potionMap.put(PotionChance.NAME, new PotionData(/*idCount++, */PotionChance.class));
		potionMap.put(PotionBless.NAME, new PotionData(/*idCount++, */PotionBless.class));
		potionMap.put(PotionCurse.NAME, new PotionData(/*idCount++, */PotionCurse.class));
		potionMap.put(PotionFlight.NAME, new PotionData(/*idCount++, */PotionFlight.class));
		potionMap.put(PotionTeleport.NAME, new PotionData(/*idCount++, */PotionTeleport.class));
		potionMap.put(PotionTeleportSurface.NAME, new PotionData(/*idCount++, */PotionTeleportSurface.class));
		potionMap.put(PotionTeleportSpawn.NAME, new PotionData(/*idCount++, */PotionTeleportSpawn.class));
		potionMap.put(PotionInvert.NAME, new PotionData(/*idCount++, */PotionInvert.class));
		potionMap.put(PotionRevival.NAME, new PotionData(/*idCount++, */PotionRevival.class));
	}
	
	public static void loadPotionEffects() {
		
		for(String name : potionMap.keySet()) {
			PotionData data = potionMap.get(name);
			
			if(!data.enabled) {
				continue;
			}
			
			//If the potion is enabled, create it
			try {
				data.potion = data.potionClass.newInstance();
			} catch (InstantiationException e) {
				FMLLog.severe("[Potion Core] Failed to initialize potion %s", name);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				FMLLog.severe("[Potion Core] Failed to initialize potion %s", name);
				e.printStackTrace();
			}
		}
	}
	
}
