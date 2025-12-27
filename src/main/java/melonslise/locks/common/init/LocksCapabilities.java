package melonslise.locks.common.init;

import melonslise.locks.common.capability.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public final class LocksCapabilities
{
	private LocksCapabilities() {}

	public static void onWorldLoad(LevelEvent.Load event)
	{
		if (event.getLevel() instanceof Level level && !level.isClientSide()) {
			if (!level.hasData(LocksAttachments.LOCKABLE_HANDLER)) {
				level.setData(LocksAttachments.LOCKABLE_HANDLER, new LockableHandler(level));
			}
		}
	}

	public static void onChunkLoad(ChunkEvent.Load event)
	{
		if (event.getChunk() instanceof LevelChunk chunk) {
			if (!chunk.hasData(LocksAttachments.LOCKABLE_STORAGE)) {
				chunk.setData(LocksAttachments.LOCKABLE_STORAGE, new LockableStorage(chunk));
			}
		}
	}

	public static void onEntityJoin(EntityJoinLevelEvent event)
	{
		if (event.getEntity() instanceof Player player) {
			if (!player.hasData(LocksAttachments.SELECTION)) {
				player.setData(LocksAttachments.SELECTION, new Selection());
			}
		}
	}
}