package com.tmtravlr.potioncore.potion;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.tmtravlr.potioncore.PotionCore;
import com.tmtravlr.potioncore.PotionCoreEffects;
import com.tmtravlr.potioncore.PotionCoreHelper;
import com.tmtravlr.potioncore.PotionCoreEffects.PotionData;

public class ItemPotionCorePotion extends ItemPotion {
	
	public static final int SPLASH_META = 16384;
	public static ItemPotionCorePotion instance;
	
	public ItemPotionCorePotion() {
		super();
		this.setUnlocalizedName("custom_potion");
		this.setCreativeTab(PotionCore.tabPotionCore);
	}
	
	 public String getItemStackDisplayName(ItemStack stack) 
	 {
		 if(!stack.hasTagCompound())
		 {
			 return super.getItemStackDisplayName(stack);
		 }
		 else
		 {
			 String s = "";

			 if (isSplash(stack.getMetadata()))
			 {
				 s = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
			 }

			 List<PotionEffect> list = this.getEffects(stack);

			 if (list != null && !list.isEmpty())
			 {
				 String s2 = ((PotionEffect)list.get(0)).getEffectName();
				 s2 = s2 + ".postfix";
				 return s + StatCollector.translateToLocal(s2).trim();
			 }
			 else
			 {
				 String s1 = PotionHelper.getPotionPrefix(stack.getMetadata());
				 return StatCollector.translateToLocal(s1).trim() + " " + ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name")).trim();
			 }
		 }
	 }
	 
	 /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (isSplash(itemStackIn.getMetadata()))
        {
        	ItemStack copy = itemStackIn.copy();
        	copy.stackSize = 1;
            if (!playerIn.capabilities.isCreativeMode)
            {
                --itemStackIn.stackSize;
            }

            worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote)
            {
            	EntityPotionCorePotion potionEntity = new EntityPotionCorePotion(worldIn, playerIn, copy);
                worldIn.spawnEntityInWorld(potionEntity);
                potionEntity.sendPotionToClient();
            }

            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            return itemStackIn;
        }
        else
        {
            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
            return itemStackIn;
        }
    }
	
	@Override
	public List<PotionEffect> getEffects(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("CustomPotionEffects", 9))
        {
            List<PotionEffect> list1 = Lists.<PotionEffect>newArrayList();
            NBTTagList nbttaglist = stack.getTagCompound().getTagList("CustomPotionEffects", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionCoreHelper.readPotionEffectFromTag(nbttagcompound);

                if (potioneffect != null)
                {
                    list1.add(potioneffect);
                }
            }

            return list1;
        }
        else
        {
            return getEffects(stack.getItemDamage());
        }
    }
	
	@SideOnly(Side.CLIENT)
    public boolean isEffectInstant(ItemStack stack)
    {
        List<PotionEffect> list = this.getEffects(stack);

        if (list != null && !list.isEmpty())
        {
            for (PotionEffect potioneffect : list)
            {
                if (Potion.potionTypes[potioneffect.getPotionID()].isInstant())
                {
                    return true;
                }
            }

            return false;
        }
        else
        {
            return false;
        }
    }
	
	/**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
    {
        List<PotionEffect> list = this.getEffects(stack);
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (list != null && !list.isEmpty())
        {
            for (PotionEffect potioneffect : list)
            {
                String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();

                if (map != null && map.size() > 0)
                {
                    for (Entry<IAttribute, AttributeModifier> entry : map.entrySet())
                    {
                        AttributeModifier attributemodifier = (AttributeModifier)entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        multimap.put(((IAttribute)entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
                    }
                }

                if (potioneffect.getAmplifier() > 0)
                {
                    s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                }

                if (potioneffect.getDuration() > 20)
                {
                    s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")";
                }

                if (potion.isBadEffect())
                {
                    tooltip.add(EnumChatFormatting.RED + s1);
                }
                else
                {
                    tooltip.add(EnumChatFormatting.GRAY + s1);
                }
            }
        }
        else
        {
            String s = StatCollector.translateToLocal("potion.empty").trim();
            tooltip.add(EnumChatFormatting.GRAY + s);
        }

        if (!multimap.isEmpty())
        {
            tooltip.add("");
            tooltip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));

            for (Entry<String, AttributeModifier> entry1 : multimap.entries())
            {
                AttributeModifier attributemodifier2 = (AttributeModifier)entry1.getValue();
                double d0 = attributemodifier2.getAmount();
                double d1;

                if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2)
                {
                    d1 = attributemodifier2.getAmount();
                }
                else
                {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }

                if (d0 > 0.0D)
                {
                    tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), new Object[] {ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey())}));
                }
                else if (d0 < 0.0D)
                {
                    d1 = d1 * -1.0D;
                    tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), new Object[] {ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey())}));
                }
            }
        }
    }
	
	/**
	 * Returns the color of the potion depending on the effects in it
	 */
	@SideOnly(Side.CLIENT)
	@Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
		
		if(pass > 0) {
			return 16777215;
		}
		
		if(stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("display");
			
			if(tag.hasKey("color")) {
				return tag.getInteger("color");
			}
			else {
				List list = this.getEffects(stack);

	            if (list != null && !list.isEmpty())
	            {
	                return PotionCoreHelper.getCustomPotionColor(list);
	            }
			}
		}
			
        return super.getColorFromItemStack(stack, pass);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List tabList) {
		for(PotionData data : PotionCoreEffects.potionMap.values()) {
			if(data != null && data.potion != null) {
				data.potion.getCreativeItems(tabList);
			}
		}
	}
	
}
