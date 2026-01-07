package com.min01.mss.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.mss.util.MSSUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class UpdateSpinningTagPacket 
{
	private final UUID entityUUID;
	
	public UpdateSpinningTagPacket(UUID entityUUID) 
	{
		this.entityUUID = entityUUID;
	}

	public static UpdateSpinningTagPacket read(FriendlyByteBuf buf)
	{
		return new UpdateSpinningTagPacket(buf.readUUID());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
	}
	
	public static boolean handle(UpdateSpinningTagPacket message, Supplier<NetworkEvent.Context> ctx) 
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isClient())
			{
				MSSUtil.getClientLevel(t -> 
				{
					Entity entity = MSSUtil.getEntityByUUID(t, message.entityUUID);
					if(entity.getPersistentData().contains("Spinning"))
					{
						entity.getPersistentData().remove("Spinning");
					}
				});
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
