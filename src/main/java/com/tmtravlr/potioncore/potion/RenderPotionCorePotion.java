package com.tmtravlr.potioncore.potion;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPotionCorePotion extends RenderSnowball<EntityPotionCorePotion>
{
    public RenderPotionCorePotion(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn, ItemPotionCorePotion.instance, itemRendererIn);
    }

    public ItemStack func_177082_d(EntityPotionCorePotion entityIn)
    {
        return entityIn.potion == null ? new ItemStack(ItemPotionCorePotion.instance, 1, entityIn.getPotionDamage()) : entityIn.potion;
    }
}
