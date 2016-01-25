package com.tmtravlr.potioncore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.registry.GameData;

import com.tmtravlr.potioncore.effects.*;
import com.tmtravlr.potioncore.potion.ItemPotionCorePotion;

public class PotionCoreHelper {

	//Lists used in tick updates
	public static ArrayList<EntityLivingBase> cureEntities = new ArrayList<EntityLivingBase>();
	public static ArrayList<EntityLivingBase> dispelEntities = new ArrayList<EntityLivingBase>();
	
	public static ArrayList<EntityLivingBase> blessEntities = new ArrayList<EntityLivingBase>();
	public static ArrayList<EntityLivingBase> curseEntities = new ArrayList<EntityLivingBase>();

	public static ArrayList<EntityLivingBase> invertEntities = new ArrayList<EntityLivingBase>();

	public static ArrayList<Potion> goodEffectList = new ArrayList<Potion>();
	public static ArrayList<Potion> badEffectList = new ArrayList<Potion>();
	
	public static HashMap<Potion, Potion> oppositeEffects = new HashMap<Potion, Potion>();
	
	public static final IAttribute projectileDamage = new RangedAttribute((IAttribute)null, "generic.projectileDamage", 1.0D, 0.0D, 2048.0D);

	//Loads the opposite effects for the inversion potion
	public static void loadInversions() {
		loadInversion(Potion.blindness, Potion.nightVision);
		loadInversion(Potion.damageBoost, Potion.weakness);
		loadInversion(Potion.digSpeed, Potion.digSlowdown);
		loadInversion(Potion.fireResistance, PotionFire.instance);
		loadInversion(Potion.harm, Potion.heal);
		loadInversion(Potion.hunger, Potion.saturation);
		loadInversion(Potion.jump, PotionWeight.instance);
		loadInversion(Potion.moveSlowdown, Potion.moveSpeed);
		loadInversion(Potion.poison, PotionAntidote.instance);
		loadInversion(Potion.regeneration, Potion.wither);
		loadInversion(Potion.resistance, PotionVulnerable.instance);
		loadInversion(Potion.waterBreathing, PotionDrown.instance);
		loadInversion(PotionArchery.instance, PotionKlutz.instance);
		loadInversion(PotionBless.instance, PotionCurse.instance);
		loadInversion(PotionCure.instance, PotionDispel.instance);
		loadInversion(PotionLevitate.instance, PotionSlowfall.instance);
		loadInversion(PotionRepair.instance, PotionRust.instance);
	}
	
	public static void loadInversion(Potion potion1, Potion potion2) {
		
		if(potion1 != null && potion2 != null) {
			oppositeEffects.put(potion1, potion2);
			oppositeEffects.put(potion2, potion1);
		}
	}
	
	/**
	 * Clears all positive effects from the entity
	 */
	public static void clearPositiveEffects(EntityLivingBase entity) {
		Collection<PotionEffect> effects = entity.getActivePotionEffects();
    	ArrayList<Integer> idsToRemove = new ArrayList<Integer>();
    	
    	Iterator<PotionEffect> it = effects.iterator();
    	
    	while(it.hasNext()) {
    		PotionEffect effect = it.next();
    		
    		if(!Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
    			idsToRemove.add(effect.getPotionID());
    		}
    		
    	}
    	
    	for(int id : idsToRemove) {
    		entity.removePotionEffect(id);
    	}
	}
	
	/**
	 * Clears all negative effects from the entity
	 */
	public static void clearNegativeEffects(EntityLivingBase entity) {
		Collection<PotionEffect> effects = entity.getActivePotionEffects();
    	ArrayList<Integer> idsToRemove = new ArrayList<Integer>();
    	
    	Iterator<PotionEffect> it = effects.iterator();
    	
    	while(it.hasNext()) {
    		PotionEffect effect = it.next();
    		
    		if(Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
    			idsToRemove.add(effect.getPotionID());
    		}
    		
    	}
    	
    	for(int id : idsToRemove) {
    		entity.removePotionEffect(id);
    	}
	}
	
	/**
	 * Adds a random positive effect to the entity
	 */
	public static void addPotionEffectPositive(EntityLivingBase entity) {
		int r = entity.getRNG().nextInt(PotionCoreHelper.goodEffectList.size());
		
		Potion potion = PotionCoreHelper.goodEffectList.get(r);
		
		if(potion.isInstant()) {
			entity.addPotionEffect(new PotionEffect(potion.getId(), 1));
		}
		else {
			entity.addPotionEffect(new PotionEffect(potion.getId(), 1200));
		}
	}
	
	/**
	 * Adds a random negative effect to the entity
	 */
	public static void addPotionEffectNegative(EntityLivingBase entity) {
		int r = entity.getRNG().nextInt(PotionCoreHelper.badEffectList.size());
		
		Potion potion = PotionCoreHelper.badEffectList.get(r);
		
		if(potion.isInstant()) {
			entity.addPotionEffect(new PotionEffect(potion.getId(), 1));
		}
		else {
			entity.addPotionEffect(new PotionEffect(potion.getId(), 1200));
		}
	}
	
	/**
	 * Inverts the potion effects on the entity based on the map above
	 */
	public static void invertPotionEffects(EntityLivingBase entity) {
		PotionEffect[] effects = new PotionEffect[0];
		effects = entity.getActivePotionEffects().toArray(effects);
    	
    	for(int i = 0; i < effects.length; i++) {
    		PotionEffect effect = effects[i];
    		
    		if(effect != null && Potion.potionTypes[effect.getPotionID()] != null && oppositeEffects.containsKey(Potion.potionTypes[effect.getPotionID()])) {
    			Potion potion = oppositeEffects.get(Potion.potionTypes[effect.getPotionID()]);
    			
    			int duration = effect.getDuration();
    			
    			if(potion.isInstant()) {
    				duration = 1;
    			}
    			
    			entity.removePotionEffect(effect.getPotionID());
    			entity.addPotionEffect(new PotionEffect(potion.getId(), duration, effect.getAmplifier(), effect.getIsAmbient(), effect.getIsShowParticles()));
    		}
    			
    	}
	}
	
	/**
	 * Turns this potion into an {@link ItemStack} that will have it's effect.
	 * @param potion Potion to use
	 * @param duration Duration of the potion
	 * @param amplifier Amplifier of the potion
	 * @param splash Should this be a splash potion?
	 * @return The {@link ItemStack} which has this effect
	 */
	public static ItemStack getItemStack(Potion potion, int duration, int amplifier, boolean splash) {
		ItemStack toAdd;
    	NBTTagCompound tag;
    	
    	tag = new NBTTagCompound();
		tag.setTag("CustomPotionEffects", new NBTTagList());
		tag.getTagList("CustomPotionEffects", 0).appendTag(writePotionToTag(potion, duration, amplifier));
		
		toAdd = new ItemStack(ItemPotionCorePotion.instance);
		if(splash) {
			toAdd.setItemDamage(ItemPotionCorePotion.SPLASH_META);
		}
		toAdd.setTagCompound(tag);
		
		return toAdd;
	}
	
	/**
	 * Returns an {@link NBTTagCompound} for an ItemStack for an {@link ItemPotionCorePotion}
	 * @param potion Potion to use
	 * @param duration Duration of the potion
	 * @param amplifier Amplifier of the potion
	 * @return Tag with this effect
	 */
	public static NBTTagCompound writePotionToTag(Potion potion, int duration, int amplifier) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Id", GameData.getPotionRegistry().getNameForObject(potion).toString());
		tag.setInteger("Amplifier", amplifier);
		tag.setInteger("Duration", duration);
		return tag;
	}
	
	/**
     * Reads a custom potion effect from a potion item's NBT data, with support
     * for the new resource locations as the Id.
     */
    public static PotionEffect readPotionEffectFromTag(NBTTagCompound tag)
    {
        int id = 0;
        
        if(tag.hasKey("Id", 8)) {
        	String idString = tag.getString("Id");
        	Potion potion = Potion.getPotionFromResourceLocation(idString);
        	if(potion != null) {
        		id = potion.getId();
        	}
        	else {
        		return null;
        	}
        }
        else {
        	id = tag.getByte("Id") & 0xff;
        }

        if (id >= 0 && id < Potion.potionTypes.length && Potion.potionTypes[id] != null)
        {
            int j = tag.getByte("Amplifier");
            int k = tag.getInteger("Duration");
            boolean flag = tag.getBoolean("Ambient");
            boolean flag1 = true;

            if (tag.hasKey("ShowParticles", 1))
            {
                flag1 = tag.getBoolean("ShowParticles");
            }

            return new PotionEffect(id, k, j, flag, flag1);
        }
        else
        {
            return null;
        }
    }
    
    /**
	 * Given a {@link Collection}<{@link PotionEffect}> will return an Integer color.
	 */
	public static int getCustomPotionColor(Collection<PotionEffect> list) {
		int i = 3694022;

		if (list != null && !list.isEmpty()) {
			
			float red = -1.0F;
			float green = -1.0F;
			float blue = -1.0F;
			float count = 0;
			Iterator<PotionEffect> iterator = list.iterator();

			while (iterator.hasNext()) {
				
				PotionEffect potioneffect = iterator.next();
				int currentPotionColor = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();

				float currentRed = (float)(currentPotionColor >> 16 & 255) / 255.0F;
				float currentGreen = (float)(currentPotionColor >> 8 & 255) / 255.0F;
				float currentBlue = (float)(currentPotionColor >> 0 & 255) / 255.0F;

				for(int k = 0; k < potioneffect.getAmplifier()+1; ++k) {
					if(red < 0) {
						red = currentRed;
						green = currentGreen;
						blue = currentBlue;
					}
					else {
						red += currentRed;
						green += currentGreen;
						blue += currentBlue;
						
					}
					count++;
				}
			}

			red = red / count * 255.0F;
			green = green / count * 255.0F;
			blue = blue / count * 255.0F;
			return (int)red << 16 | (int)green << 8 | (int)blue;
		}
		else
		{
			return i;
		}
	}
}
