package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.EntityLivingBase;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Explodes at your position but does no damage to you.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionExplosionSelf extends PotionCorePotion {
	
	public static final String NAME = "explodeself";
	public static PotionExplosionSelf instance = null;
	
	public PotionExplosionSelf() {
		super(NAME, false, 0x666666);
		instance = this;
	}

    @Override
    public boolean isInstant() {
        return true;
    }
	
	@Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
		
		float strength = (amplifier + 1) * 2;
    	
		if(!entity.worldObj.isRemote) {
			entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, strength, false);
		}
    }
}
