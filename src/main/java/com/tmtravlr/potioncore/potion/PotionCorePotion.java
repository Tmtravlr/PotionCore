package com.tmtravlr.potioncore.potion;

import java.util.List;

import com.tmtravlr.potioncore.PotionCore;
import com.tmtravlr.potioncore.PotionCoreHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionCorePotion extends Potion {
	
	public ResourceLocation icon;
	
	public PotionCorePotion(String potionName, boolean bad, int color) {
		super(new ResourceLocation(PotionCore.MOD_ID, potionName), bad, color);
		this.setPotionName("potion."+potionName);
		this.icon = new ResourceLocation(PotionCore.MOD_ID, "textures/gui/potion/"+potionName+".png");
    }
	
	/**
     * checks if Potion effect is ready to be applied this tick.
     */
	@Override
    public boolean isReady(int duration, int amplifier)
    {
    	return true;
    }
	
	/**
	 * Returns true if this potion has an affect that changes per level (like swiftness, and not like night vision)
	 */
	public boolean canAmplify() {
		return true;
	}
	
	public void affectEntity(Entity thrownPotion, Entity thrower, EntityLivingBase entity, int amplifier, double potency) {
		this.performEffect(entity, amplifier);
	}

	/**
     * Returns true if the potion has a associated status icon to display in then inventory when active.
     */
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon()
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public void getCreativeItems(List list) {
    	if(this.isInstant()) {
    		list.add(PotionCoreHelper.getItemStack(this, 1, 0, false));
    		
    		if(this.canAmplify()) {
    			list.add(PotionCoreHelper.getItemStack(this, 1, 1, false));
    		}
    		
    		list.add(PotionCoreHelper.getItemStack(this, 1, 0, true));
    		
    		if(this.canAmplify()) {
    			list.add(PotionCoreHelper.getItemStack(this, 1, 1, true));
    		}
    	}
    	else {
    		list.add(PotionCoreHelper.getItemStack(this, 3*60*20, 0, false));
    		list.add(PotionCoreHelper.getItemStack(this, 8*60*20, 0, false));
    		
    		if(this.canAmplify()) {
    			list.add(PotionCoreHelper.getItemStack(this, 90*20, 1, false));
    		}
    		
    		list.add(PotionCoreHelper.getItemStack(this, 135*20, 0, true));
    		list.add(PotionCoreHelper.getItemStack(this, 6*60*20, 0, true));
    		
    		if(this.canAmplify()) {
    			list.add(PotionCoreHelper.getItemStack(this, 67*20, 1, true));
    		}
    	}
    }
	
	/**
     * Called to draw the this Potion onto the player's inventory when it's active.
     * This can be used to e.g. render Potion icons from your own texture.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param effect the active PotionEffect
     * @param mc the Minecraft instance, for convenience
     */
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int posX, int posZ, PotionEffect effect, net.minecraft.client.Minecraft mc) { 
    	mc.getTextureManager().bindTexture(icon);
        //mc.effectRenderer.drawTexturedModalRect(x + 6, y + 7, 0, 198, 18, 18);
    	int x = posX + 6;
    	int y = posZ + 7;
    	int width = 18;
    	int height = 18;
        float widthRatio = 1.0f / 18.0f;
        float heightRatio = 1.0f / 18.0f;
        net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        net.minecraft.client.renderer.WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + height), 0).tex(0, (double)((float)height * heightRatio)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0).tex((double)((float)width * widthRatio), (double)((float)height * heightRatio)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), 0).tex((double)((float)width * widthRatio), 0).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), 0).tex(0, 0).endVertex();
        tessellator.draw();
    }
	
}
