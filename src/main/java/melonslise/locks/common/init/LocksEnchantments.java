package melonslise.locks.common.init;

import melonslise.locks.Locks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public final class LocksEnchantments
{
	// Enchantments in 1.21 are data-driven via JSON files
	// These are ResourceKeys that reference the data files
	
	public static final ResourceKey<Enchantment> SHOCKING = key("shocking");
	public static final ResourceKey<Enchantment> STURDY = key("sturdy");
	public static final ResourceKey<Enchantment> COMPLEXITY = key("complexity");

	private LocksEnchantments() {}

	public static void register()
	{
		// No-op: Enchantments are now registered via datapack JSON files
		// See src/main/resources/data/locks/enchantment/
	}

	private static ResourceKey<Enchantment> key(String name)
	{
		return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Locks.ID, name));
	}
	
	// Helper to get enchantment from registry
	public static Enchantment get(ResourceKey<Enchantment> key)
	{
		return null; // TODO: Implement registry lookup when needed
	}
}