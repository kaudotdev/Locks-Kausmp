package melonslise.locks.common.init;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.stream.Stream;

public final class LocksPacketDistributors
{
	// TODO: Packet distributors changed in NeoForge 1.21
	// Not currently used, will need reimplementation if needed
	/*
	public static final PacketDistributor<Stream<LevelChunk>> TRACKING_AREA = new PacketDistributor<>((pd, s) ->
		pkt ->
		{
			// Convert each chunk to a stream of tracking players
			// Merge all streams into one
			// Remove duplicate players
			// Send packet
			s.get()
				.flatMap(chunk -> ((ServerChunkCache) chunk.getLevel().getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false).stream())
				.distinct()
				.forEach(p -> p.connection.send(pkt));
		}, NetworkDirection.PLAY_TO_CLIENT);
	*/

	private LocksPacketDistributors() {}
}