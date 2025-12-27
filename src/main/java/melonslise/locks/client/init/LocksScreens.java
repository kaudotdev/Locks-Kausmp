package melonslise.locks.client.init;

import melonslise.locks.Locks;
import melonslise.locks.client.gui.KeyRingScreen;
import melonslise.locks.client.gui.LockPickingScreen;
import melonslise.locks.common.init.LocksContainerTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@OnlyIn(Dist.CLIENT)
public final class LocksScreens
{
	private LocksScreens() {}

	public static void register(IEventBus modBus)
	{
		modBus.addListener(LocksScreens::onRegisterMenuScreens);
	}

	private static void onRegisterMenuScreens(RegisterMenuScreensEvent event)
	{
		event.register(LocksContainerTypes.LOCK_PICKING.get(), LockPickingScreen::new);
		event.register(LocksContainerTypes.KEY_RING.get(), KeyRingScreen::new);
	}
}