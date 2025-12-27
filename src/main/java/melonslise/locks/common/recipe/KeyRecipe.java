package melonslise.locks.common.recipe;

import melonslise.locks.common.init.LocksItems;
import melonslise.locks.common.init.LocksRecipeSerializers;
import melonslise.locks.common.item.LockingItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class KeyRecipe extends CustomRecipe
{
	public KeyRecipe(CraftingBookCategory pCategory)
	{
		super(pCategory);
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return LocksRecipeSerializers.KEY.get();
	}

	@Override
	public boolean matches(CraftingInput input, Level world)
	{
		boolean hasLocking = false;
		int blanks = 0;

		for(int a = 0; a < input.size(); ++a)
		{
			ItemStack stack = input.getItem(a);
			if(stack.isEmpty())
				continue;
			// Updated for 1.21: ItemStack.hasTag() no longer exists, using has(DataComponents.CUSTOM_DATA)
			if(stack.has(DataComponents.CUSTOM_DATA) && stack.get(DataComponents.CUSTOM_DATA).contains(LockingItem.KEY_ID))
			{
				if(hasLocking)
					return false;
				hasLocking = true;
			}
			else if(stack.getItem() == LocksItems.KEY_BLANK.get())
				++blanks;
			else
				return false;
		}
		return hasLocking && blanks >= 1;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries)
	{
		ItemStack locking = ItemStack.EMPTY;
		int blanks = 0;

		for (int a = 0; a < input.size(); ++a)
		{
			ItemStack stack = input.getItem(a);
			if (stack.isEmpty())
				continue;
			// Updated for 1.21: ItemStack.hasTag() no longer exists
			if (stack.has(DataComponents.CUSTOM_DATA) && stack.get(DataComponents.CUSTOM_DATA).contains(LockingItem.KEY_ID))
			{
				if (!locking.isEmpty())
					return ItemStack.EMPTY;
				locking = stack;
			}
			else if(stack.getItem() == LocksItems.KEY_BLANK.get())
				++blanks;
			else
				return ItemStack.EMPTY;
		}

		if(!locking.isEmpty() && blanks >= 1)
			return LockingItem.copyId(locking, new ItemStack(LocksItems.KEY.get()));
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input)
	{
		NonNullList<ItemStack> list = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for (int a = 0; a < list.size(); ++a)
		{
			ItemStack stack = input.getItem(a);
			// Updated for 1.21: ItemStack.hasTag() no longer exists
			if(!stack.has(DataComponents.CUSTOM_DATA) || !stack.get(DataComponents.CUSTOM_DATA).contains(LockingItem.KEY_ID))
				continue;
			list.set(a, stack.copy());
			break;
		}

		return list;
	}

	@Override
	public boolean canCraftInDimensions(int x, int y)
	{
		return x >= 3 && y >= 3;
	}
}