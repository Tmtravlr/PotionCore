package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionAttackDamage;
import net.minecraft.util.ResourceLocation;

public class PotionAttackDamageModified extends PotionAttackDamage {
	
	public static float modifier = 0.75f;
	
	public PotionAttackDamageModified(int id)
    {
        super(id, new ResourceLocation("strength"), false, 9643043);
        this.setIconIndex(4, 0);
    }

    public double getAttributeModifierAmount(int amplifier, AttributeModifier attribute)
    {
        return modifier * (double)(amplifier + 1);
    }
    
}
