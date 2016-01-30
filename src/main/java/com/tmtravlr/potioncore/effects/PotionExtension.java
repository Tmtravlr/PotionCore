package com.tmtravlr.potioncore.effects;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Extends the duration of another potion effect on you.<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionExtension extends PotionCorePotion {
	
	public static final String NAME = "extension";
	public static final String TAG_POISON = "potioncore - extend poison";
	public static final String TAG_WITHER = "potioncore - extend wither";
	public static final String TAG_REGEN = "potioncore - extend regen";
	public static PotionExtension instance = null;

	public static String[] blacklist = {};
	public static final ArrayList<Potion> potionBlacklist = new ArrayList<Potion>();
	
	public PotionExtension() {
		super(NAME, false, 0x990099);
		instance = this;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
		ArrayList<PotionEffect> potionList = new ArrayList<PotionEffect>(entity.getActivePotionEffects());
		potionList.remove(entity.getActivePotionEffect(this));
		for (Potion potion : potionBlacklist) {
			potionList.remove(entity.getActivePotionEffect(potion));
		}
		
		PotionEffect effect;
		for (int i = amplifier+1; potionList.size() > 0 && i-- > 0;) {
			effect = potionList.remove(entity.getRNG().nextInt(potionList.size()));
			if (effect.getPotionID() == Potion.poison.id) {
				extendPeriodicEffect(TAG_POISON, 25, entity, effect);
			}
			else if (effect.getPotionID() == Potion.wither.id) {
				extendPeriodicEffect(TAG_WITHER, 40, entity, effect);
			}
			else if (effect.getPotionID() == Potion.regeneration.id) {
				extendPeriodicEffect(TAG_REGEN, 50, entity, effect);
			}
			else if (effect.getPotionID() != this.id && !Potion.potionTypes[effect.getPotionID()].isInstant()) {
				effect.combine(new PotionEffect(effect.getPotionID(), effect.getDuration() + 1, effect.getAmplifier(), effect.getIsAmbient(), effect.getIsShowParticles()));
			}
		}
	}
    
    private void extendPeriodicEffect(String tag, int period, EntityLivingBase entity, PotionEffect effect) {
    	int extraDuration = entity.getEntityData().getInteger(tag) + 1;
    	
        period = period >> effect.getAmplifier();
        if (extraDuration >= period) {
        	extraDuration -= period;
			effect.combine(new PotionEffect(effect.getPotionID(), effect.getDuration() + period, effect.getAmplifier(), effect.getIsAmbient(), effect.getIsShowParticles()));
        }
        
        entity.getEntityData().setInteger(tag, extraDuration);
    }
}
