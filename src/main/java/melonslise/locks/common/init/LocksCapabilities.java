package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.capability.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod.EventBusSubscriber(modid = Locks.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LocksCapabilities
{
	private LocksCapabilities() {}

	@SubscribeEvent
	public static void onWorldLoad(LevelEvent.Load event)
	{
		if (event.getLevel() instanceof Level level && !level.isClientSide()) {
			if (!level.hasData(LocksAttachments.LOCKABLE_HANDLER)) {
				level.setData(LocksAttachments.LOCKABLE_HANDLER, new LockableHandler(level));
			}
		}
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event)
	{
		if (event.getChunk() instanceof LevelChunk chunk) {
			if (!chunk.hasData(LocksAttachments.LOCKABLE_STORAGE)) {
				chunk.setData(LocksAttachments.LOCKABLE_STORAGE, new LockableStorage(chunk));
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent event)
	{
		if (event.getEntity() instanceof Player player) {
			if (!player.hasData(LocksAttachments.SELECTION)) {
				player.setData(LocksAttachments.SELECTION, new Selection());
			}
		}
	}
}