package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Lets you fly like in creative mode.<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionFlight extends PotionCorePotion {

	public static final String NAME = "flight";
	public static final String TAG_NAME = "potion core - flight";
	public static PotionFlight instance = null;
	
	public PotionFlight() {
		super(NAME, false, 0x5599FF);
		instance = this;
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
}
