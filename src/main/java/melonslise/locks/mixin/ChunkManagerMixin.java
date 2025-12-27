package melonslise.locks.mixin;

import melonslise.locks.common.init.LocksAttachments;
import melonslise.locks.common.init.LocksNetwork;
import melonslise.locks.common.network.toclient.AddLockableToChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public class ChunkManagerMixin
{
	@Inject(at = @At("TAIL"), method = "playerLoadedChunk")
	private void playerLoadedChunk(ServerPlayer player, MutableObject<ClientboundLevelChunkWithLightPacket> pkts, LevelChunk ch, CallbackInfo ci)
	{
		ch.getData(LocksAttachments.LOCKABLE_STORAGE).get().values()
			.forEach(lkb -> PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) player.level(), ch.getPos(), new AddLockableToChunkPacket(lkb, ch)));
	}
}