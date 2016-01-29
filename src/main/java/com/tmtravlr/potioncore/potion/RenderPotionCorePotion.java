package com.tmtravlr.potioncore.potion;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPotionCorePotion extends RenderSnowball
{
    public RenderPotionCorePotion(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn, ItemPotionCorePotion.instance, itemRendererIn);
    }
    
    public ItemStack getItem(EntityPotionCorePotion potionEntity)
    {
        return potionEntity.potion == null ? new ItemStack(Items.potionitem, 1, potionEntity.getPotionDamage()) : potionEntity.potion;
    }

    public ItemStack func_177082_d(Entity entity)
    {
        return this.getItem((EntityPotionCorePotion)entity);
    }
}
