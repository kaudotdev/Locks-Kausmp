package melonslise.locks.client.event;

import melonslise.locks.Locks;
import melonslise.locks.client.init.LocksItemModelsProperties;
import melonslise.locks.client.init.LocksScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public final class LocksClientModEvents
{
	private LocksClientModEvents() {}

	public static void register(IEventBus modBus)
	{
		modBus.addListener(LocksClientModEvents::onSetup);
	}

	private static void onSetup(FMLClientSetupEvent e)
	{
		LocksScreens.register();
		LocksItemModelsProperties.register();
	}
}