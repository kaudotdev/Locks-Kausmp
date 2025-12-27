package melonslise.locks;

import melonslise.locks.client.event.LocksClientModEvents;
import melonslise.locks.common.config.LocksClientConfig;
import melonslise.locks.common.config.LocksCommonConfig;
import melonslise.locks.common.config.LocksServerConfig;
import melonslise.locks.common.init.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Locks.ID)
public final class Locks
{
	public static final String ID = "locks";

	public static final Logger LOGGER = LogManager.getLogger();

	public Locks(IEventBus modBus, ModContainer container)
	{
		ModLoadingContext.get().registerConfig(Type.SERVER, LocksServerConfig.SPEC);
		ModLoadingContext.get().registerConfig(Type.COMMON, LocksCommonConfig.SPEC);
		ModLoadingContext.get().registerConfig(Type.CLIENT, LocksClientConfig.SPEC);
		
		LocksAttachments.register(modBus);
		LocksNetwork.register(modBus);
		LocksItems.register();
		LocksEnchantments.register();
		LocksSoundEvents.register();
		LocksContainerTypes.register();
		LocksRecipeSerializers.register();
		LocksFeatures.register();
		
		// Register client-side events
		if (FMLEnvironment.dist == Dist.CLIENT) {
			LocksClientModEvents.register(modBus);
		}
	}
}