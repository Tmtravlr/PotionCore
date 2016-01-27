package com.tmtravlr.potioncore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public EntityPlayer getPlayer() {
		return null;
	}
	
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new PotionCoreEventHandler());
		//FMLCommonHandler.instance().bus().register(new PotionCoreEventHandler());
	}
	
	public void registerRenderers() {}
	
	public void loadInverted() {}
	
	public void doPotionSmashEffects(BlockPos pos, ItemStack stack) {}
	
}
