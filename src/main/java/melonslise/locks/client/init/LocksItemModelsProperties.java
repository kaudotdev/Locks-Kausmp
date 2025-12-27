package melonslise.locks.client.init;

import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksItems;
import melonslise.locks.common.item.LockItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class LocksItemModelsProperties
{
	private LocksItemModelsProperties() {}

	public static void register()
	{
		// TODO: Implement with Item Data Attachments
		// KeyRing inventory needs to be migrated to NeoForge item data attachments
		ItemProperties.register(LocksItems.KEY_RING.get(), ResourceLocation.fromNamespaceAndPath(Locks.ID, "keys"), (stack, world, entity, speed) ->
		{
			// Temporarily return 0 until item data attachment is implemented
			return 0.0f;
			/* OLD CODE using ForgeCapabilities:
			return stack.getCapability(ForgeCapabilities.ITEM_HANDLER)
				.map(inv ->
				{
					int keys = 0;
					for(int a = 0; a < inv.getSlots(); ++a)
						if(!inv.getStackInSlot(a).isEmpty())
							++keys;
					return (float) keys / inv.getSlots();
				})
				.orElse(0f);
			*/
		});
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Locks.ID, "open");
		ItemPropertyFunction getter = (stack, world, entity, speed) -> LockItem.isOpen(stack) ? 1f : 0f;
		ItemProperties.register(LocksItems.WOOD_LOCK.get(), id, getter);
		ItemProperties.register(LocksItems.IRON_LOCK.get(), id, getter);
		ItemProperties.register(LocksItems.STEEL_LOCK.get(), id, getter);
		ItemProperties.register(LocksItems.GOLD_LOCK.get(), id, getter);
		ItemProperties.register(LocksItems.DIAMOND_LOCK.get(), id, getter);
	}
}