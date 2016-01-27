package com.tmtravlr.potioncore;

import java.util.Random;

import com.tmtravlr.potioncore.potion.EntityPotionCorePotion;
import com.tmtravlr.potioncore.potion.ItemPotionCorePotion;
import com.tmtravlr.potioncore.potion.RenderPotionCorePotion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	Random random = new Random();

	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	public void registerEventHandlers() {
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new PotionCoreEventHandlerClient());
	}
	
	public void registerRenderers() {
		RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
		
		renderer.getItemModelMesher().register(ItemPotionCorePotion.instance, new ItemMeshDefinition()
        {
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
            	return ItemPotion.isSplash(stack.getMetadata()) ? new ModelResourceLocation("bottle_splash", "inventory") : new ModelResourceLocation("bottle_drinkable", "inventory");
            }
        });
		
		RenderingRegistry.registerEntityRenderingHandler(EntityPotionCorePotion.class, new RenderPotionCorePotion(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()));
	}
	
	public void loadInverted() {
		PotionCoreEventHandlerClient.loadInverted();
	}
	
	public void doPotionSmashEffects(BlockPos pos, ItemStack stack) {
		if(stack == null) {
			return;
		}
		
		double d13 = (double)pos.getX();
        double d14 = (double)pos.getY();
        double d16 = (double)pos.getZ();

        int j1 = PotionCoreHelper.getCustomPotionColor(ItemPotionCorePotion.instance.getEffects(stack));
        float f = (float)(j1 >> 16 & 255) / 255.0F;
        float f1 = (float)(j1 >> 8 & 255) / 255.0F;
        float f2 = (float)(j1 >> 0 & 255) / 255.0F;
        EnumParticleTypes enumparticletypes = EnumParticleTypes.SPELL;

        if (ItemPotionCorePotion.instance.isEffectInstant(stack))
        {
            enumparticletypes = EnumParticleTypes.SPELL_INSTANT;
        }

        for (int l1 = 0; l1 < 100; ++l1)
        {
            double d22 = random.nextDouble() * 4.0D;
            double d23 = random.nextDouble() * Math.PI * 2.0D;
            double d24 = Math.cos(d23) * d22;
            double d9 = 0.01D + random.nextDouble() * 0.5D;
            double d11 = Math.sin(d23) * d22;
            EntityFX entityfx = spawnPotionParticle(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d13 + d24 * 0.1D, d14 + 0.3D, d16 + d11 * 0.1D, d24, d9, d11, new int[0]);

            if (entityfx != null)
            {
                float f3 = 0.75F + random.nextFloat() * 0.25F;
                entityfx.setRBGColorF(f * f3, f1 * f3, f2 * f3);
                entityfx.multiplyVelocity((float)d22);
            }
        }

	}
	
	private EntityFX spawnPotionParticle(int type, boolean ignoreRange, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            int particleSetting = mc.gameSettings.particleSetting;

            if (particleSetting == 1 && mc.theWorld.rand.nextInt(3) == 0)
            {
                particleSetting = 2;
            }

            double offsetX = mc.getRenderViewEntity().posX - posX;
            double offsetY = mc.getRenderViewEntity().posY - posY;
            double offsetZ = mc.getRenderViewEntity().posZ - posZ;

            if (ignoreRange)
            {
                return mc.effectRenderer.spawnEffectParticle(type, posX, posY, posZ, speedX, speedY, speedZ, parameters);
            }
            else
            {
                double d3 = 16.0D;
                return offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ > 256.0D ? null : (particleSetting > 1 ? null : mc.effectRenderer.spawnEffectParticle(type, posX, posY, posZ, speedX, speedY, speedZ, parameters));
            }
        }
        else
        {
            return null;
        }
	}
}
