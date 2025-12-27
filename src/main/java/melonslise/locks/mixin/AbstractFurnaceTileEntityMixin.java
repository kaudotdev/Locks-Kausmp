package melonslise.locks.mixin;

// TODO: This mixin was for the old Forge Capabilities system.
// Since we migrated to NeoForge Data Attachments, this is no longer needed.
// The capability system has been completely replaced in NeoForge 1.21.
// Keeping this file for reference but disabled.

/*
import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceTileEntityMixin
{
	@Inject(at = @At("HEAD"), method = "getCapability", cancellable = true, remap = false)
	private void getCapability(Capability cap, Direction side, CallbackInfoReturnable<LazyOptional> cir)
	{
		BlockEntity te = (BlockEntity) (Object) this;
		if(!te.isRemoved() && cap == ForgeCapabilities.ITEM_HANDLER && te.hasLevel() && LocksUtil.locked(te.getLevel(), te.getBlockPos()))
			cir.setReturnValue(LazyOptional.empty());
	}
}
*/