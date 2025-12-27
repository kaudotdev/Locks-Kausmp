package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.worldgen.feature.LocksFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LocksFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.FEATURE, Locks.ID);

    public static final DeferredHolder<Feature<?>, LocksFeature> BOULDER_TRAP = FEATURES.register("locks", () -> new LocksFeature(NoneFeatureConfiguration.CODEC));
    
    public static void register(IEventBus bus)
    {
        FEATURES.register(bus);
    }
}
