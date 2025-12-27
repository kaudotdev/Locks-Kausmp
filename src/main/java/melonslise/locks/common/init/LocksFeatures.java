package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.worldgen.feature.LocksFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class LocksFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.FEATURE, Locks.ID);

    public static final RegistryObject<LocksFeature> BOULDER_TRAP = FEATURES.register("locks", () -> new LocksFeature(NoneFeatureConfiguration.CODEC));
    public static void register()
    {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
