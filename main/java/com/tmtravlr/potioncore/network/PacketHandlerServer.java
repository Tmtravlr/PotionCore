package com.tmtravlr.potioncore.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerServer implements IMessageHandler<CToSMessage,IMessage> {

	//Types of packets

	public static final int CLIMB_FALL = 1;

	/**
	 * Handles Server Side Packets. Only returns null.
	 */
	@Override
	public IMessage onMessage(CToSMessage packet, MessageContext context)
	{
		ByteBuf buff = Unpooled.wrappedBuffer(packet.getData());

		int type = buff.readInt();
		
		switch(type) {
		case CLIMB_FALL: { 
			
			Entity player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(new UUID(buff.readLong(), buff.readLong()));
			
			if(player != null) {
				player.fallDistance = 0.0f;
			}
			break;
		}
		default:
			//do nothing.
		}


		return null;
	}
}
