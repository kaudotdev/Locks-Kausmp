package melonslise.locks.common.event;

import melonslise.locks.Locks;
import melonslise.locks.common.config.LocksCommonConfig;
import melonslise.locks.common.config.LocksServerConfig;
import net.neoforged.bus.api.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = Locks.ID)
public final class LocksModEvents
{
	private LocksModEvents() {}

	// Network registration now handled by LocksNetwork @SubscribeEvent

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent e)
	{
		if(e.getConfig().getSpec() == LocksCommonConfig.SPEC)
			LocksCommonConfig.init();
		if(e.getConfig().getSpec() == LocksServerConfig.SPEC)
			LocksServerConfig.init();
	}
}