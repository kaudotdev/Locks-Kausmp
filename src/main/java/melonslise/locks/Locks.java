package melonslise.locks;

import melonslise.locks.client.event.LocksClientForgeEvents;
import melonslise.locks.client.event.LocksClientModEvents;
import melonslise.locks.client.init.LocksScreens;
import melonslise.locks.common.config.LocksClientConfig;
import melonslise.locks.common.config.LocksCommonConfig;
import melonslise.locks.common.config.LocksServerConfig;
import melonslise.locks.common.event.LocksForgeEvents;
import melonslise.locks.common.event.LocksModEvents;
import melonslise.locks.common.init.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Locks.ID)
public final class Locks
{
	public static final String ID = "locks";

	public static final Logger LOGGER = LogManager.getLogger();

	public Locks(IEventBus modBus, ModContainer container)
	{
		container.registerConfig(ModConfig.Type.SERVER, LocksServerConfig.SPEC);
		container.registerConfig(ModConfig.Type.COMMON, LocksCommonConfig.SPEC);
		container.registerConfig(ModConfig.Type.CLIENT, LocksClientConfig.SPEC);
		
		LocksAttachments.register(modBus);
		LocksNetwork.register(modBus);
		LocksItems.register(modBus);
		LocksEnchantments.register(modBus);
		LocksSoundEvents.register(modBus);
		LocksContainerTypes.register(modBus);
		LocksRecipeSerializers.register(modBus);
		LocksFeatures.register(modBus);
		
		// Register MOD bus events
		modBus.addListener(LocksModEvents::onConfigLoad);
		
		// Register FORGE bus events
		IEventBus forgeBus = NeoForge.EVENT_BUS;
		forgeBus.addListener(LocksForgeEvents::onRightClick);
		forgeBus.addListener(LocksForgeEvents::onChunkUnload);
		forgeBus.addListener(LocksForgeEvents::onPlayerTick);
		forgeBus.addListener(LocksForgeEvents::addVillagerTrades);
		forgeBus.addListener(LocksForgeEvents::addWandererTrades);
		forgeBus.addListener(LocksForgeEvents::onBlockBreaking);
		forgeBus.addListener(LocksForgeEvents::onBlockBreak);
		
		// Register Data Attachment events
		forgeBus.addListener(LocksCapabilities::onWorldLoad);
		forgeBus.addListener(LocksCapabilities::onChunkLoad);
		forgeBus.addListener(LocksCapabilities::onEntityJoin);
		
		// Register client-side events
		if (FMLEnvironment.dist == Dist.CLIENT) {
			LocksClientModEvents.register(modBus);
			LocksScreens.register(modBus);
			forgeBus.addListener(LocksClientForgeEvents::onRenderOverlay);
		}
	}
}