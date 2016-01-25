package com.tmtravlr.potioncore.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;

import com.tmtravlr.potioncore.potion.PotionCorePotion;

/**
 * Teleports you to the top block in your current x and z coordinates. Doesn't work if you are in a cave-world like the nether.<br><br>
 * Instant: no<br>
 * Amplifier affects it: yes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @Date January 2016
 */
public class PotionTeleportSurface extends PotionCorePotion {
	
	public static final String NAME = "teleportsurface";
	public static PotionTeleportSurface instance = null;
	
	public PotionTeleportSurface() {
		super(NAME, false, 0x00FF99);
		instance = this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public boolean canAmplify() {
        return false;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
    	BlockPos pos = entity.worldObj.getTopSolidOrLiquidBlock(entity.getPosition());
    	
    	if(entity.worldObj.getBlockState(pos.down()).getBlock() == Blocks.bedrock) {
    		pos = entity.getPosition();
    	}
    	
        this.teleportTo(entity, pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D);
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
            entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

            if (entity.worldObj.getCollidingBoundingBoxes(entity, entity.getEntityBoundingBox()).isEmpty())
            {
                flag = true;
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
