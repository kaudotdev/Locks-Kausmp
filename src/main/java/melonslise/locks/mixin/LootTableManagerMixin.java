package melonslise.locks.mixin;

// TODO: LootDataManager class name changed or moved in 1.21
// Need to find the new class name and update this mixin
// Disabled for now - not critical for core functionality

/*
import melonslise.locks.common.util.LocksUtil;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(LootDataManager.class)
public class LootTableManagerMixin
{
	@Inject(at = @At("HEAD"), method = "reload")
	private void setResourceManager(PreparableReloadListener.PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir)
	{
		LocksUtil.resourceManager = pResourceManager;
	}
}
*/