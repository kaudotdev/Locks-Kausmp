package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.network.toclient.*;
import melonslise.locks.common.network.toserver.TryPinPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod.EventBusSubscriber(modid = Locks.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class LocksNetwork
{
	private LocksNetwork() {}

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event)
	{
		PayloadRegistrar registrar = event.registrar(Locks.ID).versioned("1.0");
		
		// Client-bound packets
		registrar.playToClient(
			AddLockablePacket.TYPE,
			AddLockablePacket.STREAM_CODEC,
			AddLockablePacket::handle
		);
		
		registrar.playToClient(
			AddLockableToChunkPacket.TYPE,
			AddLockableToChunkPacket.STREAM_CODEC,
			AddLockableToChunkPacket::handle
		);
		
		registrar.playToClient(
			RemoveLockablePacket.TYPE,
			RemoveLockablePacket.STREAM_CODEC,
			RemoveLockablePacket::handle
		);
		
		registrar.playToClient(
			UpdateLockablePacket.TYPE,
			UpdateLockablePacket.STREAM_CODEC,
			UpdateLockablePacket::handle
		);
		
		registrar.playToClient(
			TryPinResultPacket.TYPE,
			TryPinResultPacket.STREAM_CODEC,
			TryPinResultPacket::handle
		);
		
		// Server-bound packets
		registrar.playToServer(
			TryPinPacket.TYPE,
			TryPinPacket.STREAM_CODEC,
			TryPinPacket::handle
		);
	}
}