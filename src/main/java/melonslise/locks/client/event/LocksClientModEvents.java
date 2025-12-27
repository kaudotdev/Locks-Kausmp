package melonslise.locks.client.event;

import melonslise.locks.Locks;
import melonslise.locks.client.init.LocksItemModelsProperties;
import melonslise.locks.client.init.LocksScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Locks.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class LocksClientModEvents
{
	private LocksClientModEvents() {}

	@SubscribeEvent
	public static void onSetup(FMLClientSetupEvent e)
	{
		LocksScreens.register();
		LocksItemModelsProperties.register();
	}
}