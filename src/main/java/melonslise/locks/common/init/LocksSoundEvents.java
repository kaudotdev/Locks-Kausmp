package melonslise.locks.common.init;

import melonslise.locks.Locks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public final class LocksSoundEvents
{
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT, Locks.ID);

	public static final RegistryObject<SoundEvent>
		KEY_RING = add("key_ring"),
		LOCK_CLOSE = add("lock.close"),
		LOCK_OPEN = add("lock.open"),
		LOCK_RATTLE = add("lock.rattle"),
		PIN_FAIL = add("pin.fail"),
		PIN_MATCH = add("pin.match"),
		SHOCK = add("shock");

	private LocksSoundEvents() {}

	public static void register()
	{
		SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static RegistryObject<SoundEvent> add(String name)
	{
		return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Locks.ID, name)));
	}
}