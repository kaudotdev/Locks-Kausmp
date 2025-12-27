package melonslise.locks.mixin;

import melonslise.locks.common.util.LocksUtil;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

// TODO: This mixin is no longer needed as capabilities were replaced by Data Attachments
// The lock checking is now done differently in the access control system
@Mixin(BaseContainerBlockEntity.class)
public class LockableTileEntityMixin
{
	// Mixin disabled - capability system replaced by Data Attachments
	// Lock access control is now handled through LockableHandler
}