package melonslise.locks.common.container;

import melonslise.locks.common.init.LocksContainerTypes;
import melonslise.locks.common.init.LocksSoundEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class KeyRingContainer extends AbstractContainerMenu
{
	// TODO: Remove SlotItemHandler - needs Item Data Attachment implementation
	public static class KeyRingSlot extends Slot
	{
		public final Player player;

		public KeyRingSlot(Player player, Inventory inv, int index, int x, int y)
		{
			super(inv, index, x, y);
			this.player = player;
		}

		// TODO PITCH
		@Override
		public void set(ItemStack stack)
		{
			super.set(stack);
			if(!this.player.level().isClientSide)
				this.player.level().playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), LocksSoundEvents.KEY_RING.get(), SoundSource.PLAYERS, 1f, 1f);
		}

		@Override
		public void onTake(Player player, ItemStack stack)
		{
			if(!this.player.level().isClientSide)
				this.player.level().playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), LocksSoundEvents.KEY_RING.get(), SoundSource.PLAYERS, 1f, 1f);
			super.onTake(player, stack);
		}
	}

	public final ItemStack stack;
	// TODO: Replace with Item Data Attachment
	public final int rows;

	public KeyRingContainer(int id, Player player, ItemStack stack)
	{
		super(LocksContainerTypes.KEY_RING.get(), id);
		this.stack = stack;
		// TODO: Implement KeyRing inventory using Item Data Attachments
		// Item capabilities were replaced with data attachments in NeoForge 1.21
		this.rows = 0; // Temporary - needs item data attachment implementation

		int offset = (rows - 4) * 18;
		for(int row = 0; row < 3; ++row)
			for (int col = 0; col < 9; ++col)
				this.addSlot(new Slot(player.getInventory(), col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + offset));

		for(int coll = 0; coll < 9; ++coll)
			this.addSlot(new Slot(player.getInventory(), coll, 8 + coll * 18, 161 + offset));
	}

	@Override
	public boolean stillValid(Player player)
	{
		return !this.stack.isEmpty();
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index)
	{
		// TODO: Implement when item data attachment is ready
		return ItemStack.EMPTY;
	}

	// Container factory for network-based menu opening
	public static KeyRingContainer create(int id, Inventory inv, FriendlyByteBuf buffer)
	{
		return new KeyRingContainer(id, inv.player, inv.player.getItemInHand(buffer.readEnum(InteractionHand.class)));
	}

	public static class Provider implements MenuProvider
	{
		public final ItemStack stack;

		public Provider(ItemStack stack)
		{
			this.stack = stack;
		}

		@Override
		public AbstractContainerMenu createMenu(int id, Inventory inv, Player player)
		{
			return new KeyRingContainer(id, player, this.stack);
		}

		@Override
		public Component getDisplayName()
		{
			return this.stack.getHoverName();
		}
	}

	public static class Writer implements Consumer<FriendlyByteBuf>
	{
		public final InteractionHand hand;

		public Writer(InteractionHand hand)
		{
			this.hand = hand;
		}

		@Override
		public void accept(FriendlyByteBuf buffer)
		{
			buffer.writeEnum(this.hand);
		}
	}
}