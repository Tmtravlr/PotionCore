package com.tmtravlr.potioncore;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.tmtravlr.potioncore.PotionCoreEffects.PotionData;
import com.tmtravlr.potioncore.effects.PotionDrown;

@SideOnly(Side.CLIENT)
public class PotionCoreEventHandlerClient {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderAir(RenderGameOverlayEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(event.type == RenderGameOverlayEvent.ElementType.AIR) {
			
			EntityLivingBase player = (EntityLivingBase)mc.getRenderViewEntity();
			PotionData drown = PotionCoreEffects.potionMap.get(PotionDrown.NAME);
			
			if (player != null && drown != null && drown.potion != null && player.isPotionActive(drown.potion)) {
				event.setCanceled(true);
				
				if(!player.isInsideOfMaterial(Material.water)) {
					int air = player.getEntityData().getInteger(PotionDrown.TAG_NAME);
					
					mc.mcProfiler.startSection("air");
			        GlStateManager.enableBlend();
			        int width = event.resolution.getScaledWidth();
			        int height = event.resolution.getScaledHeight();
			        int left = width / 2 + 91;
			        int top = height - GuiIngameForge.right_height;
	
			        int full = MathHelper.ceiling_double_int((double)(air - 2) * 10.0D / 300.0D);
		            int partial = MathHelper.ceiling_double_int((double)air * 10.0D / 300.0D) - full;
	
		            for (int i = 0; i < full + partial; ++i)
		            {
		            	mc.ingameGUI.drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
		            }
		            GuiIngameForge.right_height += 10;
			        
	
			        GlStateManager.disableBlend();
			        mc.mcProfiler.endSection();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onFogRender(FogDensity event) {
		
		if(ConfigLoader.fixBlindness) {
			Minecraft mc = Minecraft.getMinecraft();
			
			if(event.entity == mc.thePlayer && mc.thePlayer.isPotionActive(Potion.blindness)) {
				float f1 = 5.0F;
	            int duration = mc.thePlayer.getActivePotionEffect(Potion.blindness).getDuration();
	            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.blindness).getAmplifier()+1;
	
	            if (duration < 20)
	            {
	                f1 = 5.0F + (mc.gameSettings.renderDistanceChunks * 16 - 5.0F) * (1.0F - (float)duration / 20.0F);
	            }
	            
	            float multiplier = 0.25F / amplifier;
	
	            GlStateManager.setFog(9729);
	            
	            GlStateManager.setFogStart(f1 * multiplier);
	           	GlStateManager.setFogEnd(f1 * multiplier*4);
	            
	
	            if (GLContext.getCapabilities().GL_NV_fog_distance)
	            {
	                GL11.glFogi(34138, 34139);
	            }
				
	            event.density = 2.0f;
	            event.setCanceled(true);
			}
		}
	}
}
