package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Teleports you to your spawn point if you don't move for 10 seconds.<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionTeleportSpawn extends PotionCorePotion {

	public static final String NAME = "teleportspawn";
	public static final String TAG_NAME = "potion core - spawn teleport";
	public static final String TAG_X = "potion core - spawn teleport x";
	public static final String TAG_Y = "potion core - spawn teleport y";
	public static final String TAG_Z = "potion core - spawn teleport z";
	public static PotionTeleportSpawn instance = null;
	
	public PotionTeleportSpawn() {
		super(NAME, false, 0x9955FF);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean canAmplify() {
        return false;
    }
	
}
