package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.EntityLivingBase;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Lights you on fire.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionFire extends PotionCorePotion {

	public static final String NAME = "fire";
	public static PotionFire instance = null;
	
	public PotionFire() {
		super(NAME, true, 0xFF5500);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }
	
	@Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
    	
    	//10 seconds of fire for each level
    	int duration = amplifier+1 * 10;
    	
    	entityLivingBaseIn.setFire(duration);
    }
}
