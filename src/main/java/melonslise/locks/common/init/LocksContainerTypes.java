package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.container.KeyRingContainer;
import melonslise.locks.common.container.LockPickingContainer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class LocksContainerTypes
{
	public static final DeferredRegister<MenuType<?>>
			CONTAINER_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Locks.ID);

	public static final DeferredHolder<MenuType<?>, MenuType<LockPickingContainer>>
		LOCK_PICKING = add("lock_picking", new MenuType<>(LockPickingContainer::create, FeatureFlags.DEFAULT_FLAGS));

	public static final DeferredHolder<MenuType<?>, MenuType<KeyRingContainer>>
		KEY_RING = add("key_ring", new MenuType<>(KeyRingContainer::create, FeatureFlags.DEFAULT_FLAGS));

	private LocksContainerTypes() {}

	public static void register(IEventBus bus)
	{
		CONTAINER_TYPES.register(bus);
	}

	public static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> add(String name, MenuType<T> type)
	{
		return CONTAINER_TYPES.register(name, () -> type);
	}
}