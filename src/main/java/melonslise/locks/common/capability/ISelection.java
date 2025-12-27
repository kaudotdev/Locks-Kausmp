package melonslise.locks.common.capability;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface ISelection
{
	BlockPos get();

	void set(BlockPos pos);
}