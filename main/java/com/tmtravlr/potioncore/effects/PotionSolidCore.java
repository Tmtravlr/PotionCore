package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.SharedMonsterAttributes;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Causes you to not take knockback<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionSolidCore extends PotionCorePotion {
	
	public static final String NAME = "solidcore";
	public static PotionSolidCore instance = null;
	
	public PotionSolidCore() {
		super(NAME, false, 0x222222);
		instance = this;
		this.registerPotionAttributeModifier(SharedMonsterAttributes.knockbackResistance, "9bface7b-f0d0-4bdb-9c0c-09c3237fa99c", 1, 0);
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
}
