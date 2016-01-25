package com.tmtravlr.potioncore.effects;

import java.util.Iterator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Extends the duration of another potion effect on you.<br><br>
 * Instant: no<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionExtension extends PotionCorePotion {
	
	public static final String NAME = "extension";
	public static PotionExtension instance = null;
	
	public PotionExtension() {
		super(NAME, false, 0x990099);
		instance = this;
    }
    
    @Override
    public boolean canAmplify() {
		return false;
	}
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	int potionCount = entity.getRNG().nextInt(entity.getActivePotionEffects().size());
    	Iterator it = entity.getActivePotionEffects().iterator();
    	
    	for(PotionEffect effect : entity.getActivePotionEffects()) {
    		if(potionCount-- <= 0) {
    			if(effect.getPotionID() == this.id || Potion.potionTypes[effect.getPotionID()].isInstant()) {
    				potionCount++;
    			}
    			else {
    				ObfuscationReflectionHelper.setPrivateValue(PotionEffect.class, effect, effect.getDuration()+1, "duration", "field_76460_b");
    				break;
    			}
    		}
    	}
	}
}
