package melonslise.locks.common.item;

import melonslise.locks.Locks;
import melonslise.locks.common.config.LocksServerConfig;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.init.LocksEnchantments;
import melonslise.locks.common.util.Lock;
import melonslise.locks.common.util.Lockable;
import melonslise.locks.common.util.LocksPredicates;
import melonslise.locks.common.util.LocksUtil;
import melonslise.locks.common.util.TrustManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class LockPickItem extends Item
{
	public static final Component TOO_COMPLEX_MESSAGE = Component.translatable(Locks.ID + ".status.too_complex");
	public static final Component OWNER_ONLINE_MESSAGE = Component.translatable(Locks.ID + ".status.owner_online");
	public static final Component HAS_ACCESS_MESSAGE = Component.translatable(Locks.ID + ".status.has_access");
	private static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT = new DecimalFormat("0.##");

	public final float strength;

	public LockPickItem(float strength, Properties props)
	{
		super(props);
		this.strength = strength;
	}

	public static final String KEY_STRENGTH = "Strength";

	// WARNING: EXPECTS LOCKPICKITEM STACK
	public static float getOrSetStrength(ItemStack stack)
	{
		CompoundTag nbt = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if(!nbt.contains(KEY_STRENGTH))
		{
			nbt.putFloat(KEY_STRENGTH, ((LockPickItem) stack.getItem()).strength);
			stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
		}
		return nbt.getFloat(KEY_STRENGTH);
	}

	public static boolean canPick(ItemStack stack, int cmp)
	{
		return getOrSetStrength(stack) > cmp * 0.25f;
	}

	public static boolean canPick(ItemStack stack, Lockable lkb)
	{
		// TODO: EnchantmentHelper.getItemEnchantmentLevel API changed in 1.21
		// Need to use new enchantment system with ResourceKey
		// For now, assume complexity is 0
		return canPick(stack, 0);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx)
	{
		Level world = ctx.getLevel();
		Player player = ctx.getPlayer();
		BlockPos pos = ctx.getClickedPos();
		List<Lockable> match = LocksUtil.intersecting(world, pos).filter(LocksPredicates.LOCKED).collect(Collectors.toList());
		if(match.isEmpty())
			return InteractionResult.PASS;
		Lockable lkb = match.get(0);
		
		// Check if player already has access (owner or trusted)
		Lock lock = lkb.lock;
		if(TrustManager.hasAccess(lock, player))
		{
			if(world.isClientSide)
				player.displayClientMessage(HAS_ACCESS_MESSAGE, true);
			return InteractionResult.PASS;
		}
		
		// Check if owner is online (if config requires it)
		if(!world.isClientSide && LocksServerConfig.REQUIRE_OWNER_OFFLINE.get())
		{
			if(lock.getOwner() != null && TrustManager.isOwnerOnline(lock, world.getServer()))
			{
				player.displayClientMessage(OWNER_ONLINE_MESSAGE, true);
				return InteractionResult.FAIL;
			}
		}
		
		if(!canPick(ctx.getItemInHand(), lkb))
		{
			if(world.isClientSide)
				player.displayClientMessage(TOO_COMPLEX_MESSAGE, true);
			return InteractionResult.PASS;
		}
		if(world.isClientSide)
			return InteractionResult.SUCCESS;
		InteractionHand hand = ctx.getHand();
		player.openMenu(new LockPickingContainer.Provider(hand, lkb), buf -> {
			new LockPickingContainer.Writer(hand, lkb).accept(buf);
		});
		return InteractionResult.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag)
	{
		super.appendHoverText(stack, context, lines, flag);
		CompoundTag nbt = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		float strength = nbt.contains(KEY_STRENGTH) ? nbt.getFloat(KEY_STRENGTH) : this.strength;
		lines.add(Component.translatable(Locks.ID + ".tooltip.strength", ATTRIBUTE_MODIFIER_FORMAT.format(strength)).withStyle(ChatFormatting.DARK_GREEN));
	}
}