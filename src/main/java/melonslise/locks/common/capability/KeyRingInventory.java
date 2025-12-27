package melonslise.locks.common.capability;

import melonslise.locks.common.init.LocksItemTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

// Thanks to Gigaherz
public class KeyRingInventory implements IItemHandlerModifiable
{
	public final int size;
	public final ItemStack stack;

	public KeyRingInventory(ItemStack stack, int rows, int col)
	{
		this.size = rows * col;
		this.stack = stack;
	}

	@Override
	public int getSlots()
	{
		return this.size;
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int slot)
	{
		this.validateSlotIndex(slot);
		// Get custom data from stack
		net.minecraft.world.item.component.CustomData customData = this.stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
		CompoundTag tag = customData.copyTag();
		ListTag list = tag.getList("Items", Tag.TAG_COMPOUND);
		for(int a = 0; a < list.size(); a++)
		{
			CompoundTag nbt = list.getCompound(a);
			if(nbt.getInt("Slot") != slot)
				continue;
			// Use EMPTY registry access for simple deserialization
			return ItemStack.parseOptional(net.minecraft.core.HolderLookup.Provider.create(java.util.stream.Stream.empty()), nbt);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		this.validateSlotIndex(slot);
		CompoundTag nbt = null;
		if(!stack.isEmpty())
		{
			nbt = new CompoundTag();
			nbt.putInt("Slot", slot);
			stack.save(net.minecraft.core.HolderLookup.Provider.create(java.util.stream.Stream.empty()), nbt);
		}
		// Get or create custom data
		net.minecraft.world.item.component.CustomData customData = this.stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
		CompoundTag tag = customData.copyTag();
		ListTag list = tag.getList("Items", Tag.TAG_COMPOUND);
		for(int a = 0; a < list.size(); a++)
		{
			CompoundTag existing = list.getCompound(a);
			if(existing.getInt("Slot") != slot)
				continue;
			if(!stack.isEmpty())
				list.set(a, nbt);
			else
				list.remove(a);
			tag.put("Items", list);
			this.stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
			return;
		}
		if(!stack.isEmpty())
		{
			list.add(nbt);
			tag.put("Items", list);
			this.stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
		}
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		this.validateSlotIndex(slot);
		ItemStack existing = getStackInSlot(slot);
		int limit = stack.getMaxStackSize();
		if (!existing.isEmpty())
		{
			if (!ItemStack.isSameItemSameComponents(stack, existing))
				return stack;
			limit -= existing.getCount();
		}
		if (limit <= 0)
			return stack;
		boolean reachedLimit = stack.getCount() > limit;
		if (!simulate)
		{
			if (existing.getCount() <= 0)
				existing = reachedLimit ? stack.copyWithCount(limit) : stack;
			else
				existing.grow(reachedLimit ? limit : stack.getCount());
			this.setStackInSlot(slot, existing);
		}
		return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount == 0)
			return ItemStack.EMPTY;
		this.validateSlotIndex(slot);
		ItemStack existing = this.getStackInSlot(slot);
		if (existing.isEmpty())
			return ItemStack.EMPTY;
		int toExtract = Math.min(amount, existing.getMaxStackSize());
		if (existing.getCount() <= toExtract)
		{
			if (!simulate)
					this.setStackInSlot(slot, ItemStack.EMPTY);
			return existing;
		}
		else
		{
			if (!simulate)
				this.setStackInSlot(slot, existing.copyWithCount(existing.getCount() - toExtract));
			return existing.copyWithCount(toExtract);
		}
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	private void validateSlotIndex(int slot)
	{
		if (slot < 0 || slot >= this.getSlots())
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	{
		return stack.is(LocksItemTags.KEYS);
	}
}