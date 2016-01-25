package com.tmtravlr.potioncore.effects;

import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Lowers your projectile damage<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionKlutz extends PotionCorePotion {

	public static final String NAME = "klutz";
	public static PotionKlutz instance = null;
	
	public PotionKlutz() {
		super(NAME, false, 0x999933);
		instance = this;
		this.registerPotionAttributeModifier(PotionCoreHelper.projectileDamage, "fd747754-0718-456c-8538-330c4ab65793", -0.3, 2);
	}
}
