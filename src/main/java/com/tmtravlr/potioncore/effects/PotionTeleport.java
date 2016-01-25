package com.tmtravlr.potioncore.effects;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Teleports you to a random spot nearby.<br><br>
 * Instant: yes<br>
 * Amplifier affects it: no
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionTeleport extends PotionCorePotion {

	public static final String NAME = "teleport";
	public static PotionTeleport instance = null;
	
	public PotionTeleport() {
		super(NAME, false, 0x00CC99);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        int tries = 20;
        double posX = entity.posX;
        double posY = entity.posY;
        double posZ = entity.posZ;
        boolean success = false;
        
        while(tries > 0 && !success) {
	    	double d0 = entity.posX + (entity.getRNG().nextDouble() - 0.5D) * 16.0D * (amplifier+1);
	        double d1 = entity.posY + (double)(entity.getRNG().nextInt(64) - 32);
	        double d2 = entity.posZ + (entity.getRNG().nextDouble() - 0.5D) * 16.0D * (amplifier+1);
	        success = this.teleportTo(entity, d0, d1, d2);
	        tries--;
        }
        
        if(!success) {
        	this.teleportTo(entity, posX, posY, posZ);
        }
	}
    
    private boolean teleportTo(EntityLivingBase entity, double x, double y, double z) {
    	net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(entity, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        double d0 = entity.posX;
        double d1 = entity.posY;
        double d2 = entity.posZ;
        entity.posX = event.targetX;
        entity.posY = event.targetY;
        entity.posZ = event.targetZ;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entity.posX, entity.posY, entity.posZ);

        if (entity.worldObj.isBlockLoaded(blockpos))
        {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0)
            {
                BlockPos blockpos1 = blockpos.down();
                Block block = entity.worldObj.getBlockState(blockpos1).getBlock();

                if (block.getMaterial().blocksMovement())
                {
                    flag1 = true;
                }
                else
                {
                    --entity.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1)
            {
            	entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

                if (entity.worldObj.getCollidingBoundingBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && !entity.worldObj.isAnyLiquid(entity.getEntityBoundingBox()))
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
        	entity.setPosition(d0, d1, d2);
            return false;
        }
        else
        {
            int i = 128;

            for (int j = 0; j < i; ++j)
            {
                double d6 = (double)j / ((double)i - 1.0D);
                float f = (entity.getRNG().nextFloat() - 0.5F) * 0.2F;
                float f1 = (entity.getRNG().nextFloat() - 0.5F) * 0.2F;
                float f2 = (entity.getRNG().nextFloat() - 0.5F) * 0.2F;
                double d3 = d0 + (entity.posX - d0) * d6 + (entity.getRNG().nextDouble() - 0.5D) * (double)entity.width * 2.0D;
                double d4 = d1 + (entity.posY - d1) * d6 + entity.getRNG().nextDouble() * (double)entity.height;
                double d5 = d2 + (entity.posZ - d2) * d6 + (entity.getRNG().nextDouble() - 0.5D) * (double)entity.width * 2.0D;
                entity.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, (double)f, (double)f1, (double)f2, new int[0]);
            }

            entity.worldObj.playSoundEffect(d0, d1, d2, "mob.endermen.portal", 1.0F, 1.0F);
            entity.worldObj.playSoundAtEntity(entity, "mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }
}
