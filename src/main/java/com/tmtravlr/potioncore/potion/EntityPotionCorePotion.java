package com.tmtravlr.potioncore.potion;

import io.netty.buffer.Unpooled;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.tmtravlr.potioncore.PotionCore;
import com.tmtravlr.potioncore.network.PacketHandlerClient;
import com.tmtravlr.potioncore.network.SToCMessage;

public class EntityPotionCorePotion extends EntityPotion {
	
	public boolean smashed = false;
	public ItemStack potion;

	public EntityPotionCorePotion(World worldIn)
    {
        super(worldIn);
    }

    public EntityPotionCorePotion(World worldIn, EntityLivingBase throwerIn, int meta)
    {
        this(worldIn, throwerIn, new ItemStack(Items.potionitem, 1, meta));
    }

    public EntityPotionCorePotion(World worldIn, EntityLivingBase throwerIn, ItemStack potionIn)
    {
        super(worldIn, throwerIn, potionIn);
        potion = potionIn;
    }
    
    public void sendPotionToClient() {
    	PacketBuffer out = new PacketBuffer(Unpooled.buffer());
		
		out.writeInt(PacketHandlerClient.POTION_ENTITY);
		out.writeInt(this.getEntityId());
		out.writeItemStackToBuffer(potion);
		
		SToCMessage packet = new SToCMessage(out);
		PotionCore.networkWrapper.sendToDimension(packet, this.worldObj.provider.getDimensionId());
    }
    
    public void doSmashEffects() {
    	 PotionCore.proxy.doPotionSmashEffects(new BlockPos(this), potion);
    }
    
    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition position)
    {
        if (!this.worldObj.isRemote)
        {
            List<PotionEffect> list = ItemPotionCorePotion.instance.getEffects(potion);

            if (list != null && !list.isEmpty())
            {
                AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D);
                List<EntityLivingBase> list1 = this.worldObj.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

                if (!list1.isEmpty())
                {
                    for (EntityLivingBase entitylivingbase : list1)
                    {
                        double d0 = this.getDistanceSqToEntity(entitylivingbase);

                        if (d0 < 16.0D)
                        {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (entitylivingbase == position.entityHit)
                            {
                                d1 = 1.0D;
                            }

                            for (PotionEffect potioneffect : list)
                            {
                                int i = potioneffect.getPotionID();

                                if (Potion.potionTypes[i].isInstant())
                                {
                                    Potion.potionTypes[i].affectEntity(this, this.getThrower(), entitylivingbase, potioneffect.getAmplifier(), d1);
                                }
                                else
                                {
                                    int j = (int)(d1 * (double)potioneffect.getDuration() + 0.5D);

                                    if (j > 20)
                                    {
                                        entitylivingbase.addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.worldObj.playSoundAtEntity(this, "game.potion.smash", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            this.setDead();
        }
        else {
        	if(!smashed) {
        		doSmashEffects();
        		smashed = true;
        	}
        }
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
    	super.onUpdate();
    	
    	if(!this.worldObj.isRemote && this.ticksExisted < 5) {
    		this.sendPotionToClient();
    	}
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("Potion", 10))
        {
            this.potion = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("Potion"));
        }
        else
        {
            this.setPotionDamage(tagCompund.getInteger("potionValue"));
        }

        if (this.potion == null)
        {
            this.setDead();
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);

        if (this.potion != null)
        {
            tagCompound.setTag("Potion", this.potion.writeToNBT(new NBTTagCompound()));
        }
    }

}
