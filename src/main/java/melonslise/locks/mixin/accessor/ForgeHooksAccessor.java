package melonslise.locks.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;

// TODO: This accessor is no longer needed in NeoForge 1.21
// ForgeHooks loot context handling has changed
@Mixin(targets = "net.neoforged.neoforge.common.NeoForge")
public interface ForgeHooksAccessor
{
	// Accessor disabled - ForgeHooks API changed in NeoForge 1.21
}