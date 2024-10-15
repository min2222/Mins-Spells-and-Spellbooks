package com.min01.mss.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.mss.util.MSSClientUtil;
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

	public UpdateSpinningTagPacket(FriendlyByteBuf buf)
	{
		this.entityUUID = buf.readUUID();
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
	}
	
	public static class Handler 
	{
		public static boolean onMessage(UpdateSpinningTagPacket message, Supplier<NetworkEvent.Context> ctx) 
		{
			ctx.get().enqueueWork(() ->
			{
				Entity entity = MSSUtil.getEntityByUUID(MSSClientUtil.MC.level, message.entityUUID);
				if(entity.getPersistentData().contains("Spinning"))
				{
					entity.getPersistentData().remove("Spinning");
				}
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
