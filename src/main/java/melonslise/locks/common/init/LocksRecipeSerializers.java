package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.recipe.KeyRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.fml.ModContainer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;

public final class LocksRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Locks.ID);

	public static final RegistryObject<RecipeSerializer<KeyRecipe>> KEY = add("crafting_key", new SimpleCraftingRecipeSerializer<>(KeyRecipe::new));

	private LocksRecipeSerializers() {}

	public static void register(IEventBus bus)
	{
		RECIPE_SERIALIZERS.register(bus);
	}

	public static <T extends Recipe<?>> RegistryObject<RecipeSerializer<T>> add(String name, RecipeSerializer<T> serializer)
	{
		return RECIPE_SERIALIZERS.register(name, () -> serializer);
	}
}