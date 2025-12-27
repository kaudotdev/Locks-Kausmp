package melonslise.locks.common.event;

import melonslise.locks.Locks;
import melonslise.locks.common.config.LocksCommonConfig;
import melonslise.locks.common.config.LocksServerConfig;
import melonslise.locks.common.init.LocksNetwork;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Locks.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class LocksModEvents
{
	private LocksModEvents() {}

	@SubscribeEvent
	public static void onSetup(FMLCommonSetupEvent e)
	{
		// LocksCapabilities.register();
		LocksNetwork.register();
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent e)
	{
		if(e.getConfig().getSpec() == LocksCommonConfig.SPEC)
			LocksCommonConfig.init();
		if(e.getConfig().getSpec() == LocksServerConfig.SPEC)
			LocksServerConfig.init();
	}
}