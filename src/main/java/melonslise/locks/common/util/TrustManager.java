package melonslise.locks.common.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Set;
import java.util.UUID;

/**
 * Utility class for managing lock trust relationships
 */
public final class TrustManager
{
	private TrustManager() {}
	
	/**
	 * Check if a player has access to a lock (either as owner or trusted)
	 * @param lock The lock to check
	 * @param player The player to check
	 * @return true if the player has access
	 */
	public static boolean hasAccess(Lock lock, Player player)
	{
		if(player == null || lock == null)
			return false;
			
		UUID playerUUID = player.getUUID();
		return lock.isOwner(playerUUID) || lock.isTrusted(playerUUID);
	}
	
	/**
	 * Check if a player has access to a lock by UUID
	 * @param lock The lock to check
	 * @param playerUUID The UUID of the player to check
	 * @return true if the player has access
	 */
	public static boolean hasAccess(Lock lock, UUID playerUUID)
	{
		if(playerUUID == null || lock == null)
			return false;
			
		return lock.isOwner(playerUUID) || lock.isTrusted(playerUUID);
	}
	
	/**
	 * Add a player to the trust list of a lock
	 * @param lock The lock to modify
	 * @param player The player to add
	 * @param maxTrusted Maximum number of trusted players allowed (from config)
	 * @return true if the player was added, false if already trusted or limit reached
	 */
	public static boolean addTrustedPlayer(Lock lock, UUID player, int maxTrusted)
	{
		if(lock == null || player == null)
			return false;
			
		if(lock.getTrustedPlayers().size() >= maxTrusted)
			return false;
			
		if(lock.isTrusted(player))
			return false;
			
		lock.addTrustedPlayer(player);
		return true;
	}
	
	/**
	 * Remove a player from the trust list of a lock
	 * @param lock The lock to modify
	 * @param player The UUID of the player to remove
	 * @return true if the player was removed, false if not in list
	 */
	public static boolean removeTrustedPlayer(Lock lock, UUID player)
	{
		if(lock == null || player == null)
			return false;
			
		if(!lock.isTrusted(player))
			return false;
			
		lock.removeTrustedPlayer(player);
		return true;
	}
	
	/**
	 * Check if the owner of a lock is currently online
	 * @param lock The lock to check
	 * @param server The server instance
	 * @return true if the owner is online, false otherwise
	 */
	public static boolean isOwnerOnline(Lock lock, MinecraftServer server)
	{
		if(lock == null || server == null || lock.getOwner() == null)
			return false;
			
		ServerPlayer owner = server.getPlayerList().getPlayer(lock.getOwner());
		return owner != null;
	}
	
	/**
	 * Get the number of trusted players for a lock
	 * @param lock The lock to check
	 * @return The number of trusted players
	 */
	public static int getTrustedCount(Lock lock)
	{
		if(lock == null)
			return 0;
			
		return lock.getTrustedPlayers().size();
	}
	
	/**
	 * Check if a lock can accept more trusted players
	 * @param lock The lock to check
	 * @param maxTrusted Maximum allowed from config
	 * @return true if more players can be added
	 */
	public static boolean canAddMoreTrusted(Lock lock, int maxTrusted)
	{
		if(lock == null)
			return false;
			
		return lock.getTrustedPlayers().size() < maxTrusted;
	}
}
