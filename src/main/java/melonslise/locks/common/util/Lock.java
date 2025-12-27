package melonslise.locks.common.util;

import melonslise.locks.common.item.LockItem;
import melonslise.locks.common.item.LockingItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Lock extends Observable
{
	public final int id;
	// index is the order, value is the pin number
	protected final byte[] combo;
	protected boolean locked;
	
	// Advanced lockpicking attributes
	protected int pins; // Number of pins (3-7)
	protected int strength; // Strength level (1-5)
	protected boolean sturdy; // Reinforced lock reduces lockpick chance
	protected UUID owner; // UUID of the player who placed the lock
	protected Set<UUID> trustedPlayers; // Set of trusted player UUIDs

	//  TODO if lock is reshuffled any time other than during creation, then next time it is loaded it will have the initial combination and not the newly reshuffled one. Thankfully reshuffling like that does happen, but this should be changed if it does happen
	public final Random rng;

	public Lock(int id, int length, boolean locked)
	{
		this(id, length, locked, Math.min(Math.max(length, 3), 7), 1, false, null, new HashSet<>());
	}
	
	public Lock(int id, int length, boolean locked, int pins, int strength, boolean sturdy, UUID owner, Set<UUID> trustedPlayers)
	{
		this.id = id;
		this.rng = new Random(id);
		this.combo = this.shuffle(length);
		// this.lookup = this.inverse(this.combo);
		this.locked = locked;
		this.pins = Math.min(Math.max(pins, 3), 7);
		this.strength = Math.min(Math.max(strength, 1), 5);
		this.sturdy = sturdy;
		this.owner = owner;
		this.trustedPlayers = trustedPlayers != null ? trustedPlayers : new HashSet<>();
	}

	public static Lock from(ItemStack stack, Player placer)
	{
		Lock lock = new Lock(LockingItem.getOrSetId(stack), LockItem.getOrSetLength(stack), !LockItem.isOpen(stack));
		// Load additional properties from NBT if they exist
		if(stack.hasTag() && stack.getTag().contains("LockData"))
		{
			CompoundTag lockData = stack.getTag().getCompound("LockData");
			if(lockData.contains("Pins")) lock.pins = lockData.getInt("Pins");
			if(lockData.contains("Strength")) lock.strength = lockData.getInt("Strength");
			if(lockData.contains("Sturdy")) lock.sturdy = lockData.getBoolean("Sturdy");
			if(lockData.contains("Owner")) lock.owner = lockData.getUUID("Owner");
		}
		// Set owner if not already set and placer is provided
		if(lock.owner == null && placer != null)
		{
			lock.owner = placer.getUUID();
		}
		return lock;
	}
	
	public static Lock from(ItemStack stack)
	{
		return from(stack, null);
	}

	public static final String KEY_ID = "Id", KEY_LENGTH = "Length", KEY_LOCKED = "Locked";
	public static final String KEY_PINS = "Pins", KEY_STRENGTH = "Strength", KEY_STURDY = "Sturdy";
	public static final String KEY_OWNER = "Owner", KEY_TRUSTED = "TrustedPlayers";

	public static Lock fromNbt(CompoundTag nbt)
	{
		int pins = nbt.contains(KEY_PINS) ? nbt.getInt(KEY_PINS) : nbt.getByte(KEY_LENGTH);
		int strength = nbt.contains(KEY_STRENGTH) ? nbt.getInt(KEY_STRENGTH) : 1;
		boolean sturdy = nbt.contains(KEY_STURDY) && nbt.getBoolean(KEY_STURDY);
		UUID owner = nbt.contains(KEY_OWNER) ? nbt.getUUID(KEY_OWNER) : null;
		
		Set<UUID> trustedPlayers = new HashSet<>();
		if(nbt.contains(KEY_TRUSTED))
		{
			ListTag list = nbt.getList(KEY_TRUSTED, Tag.TAG_INT_ARRAY);
			for(int i = 0; i < list.size(); i++)
			{
				trustedPlayers.add(nbt.getUUID(KEY_TRUSTED + i));
			}
		}
		
		return new Lock(nbt.getInt(KEY_ID), nbt.getByte(KEY_LENGTH), nbt.getBoolean(KEY_LOCKED), 
			pins, strength, sturdy, owner, trustedPlayers);
	}

	public static CompoundTag toNbt(Lock lock)
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt(KEY_ID, lock.id);
		nbt.putByte(KEY_LENGTH, (byte) lock.combo.length);
		nbt.putBoolean(KEY_LOCKED, lock.locked);
		nbt.putInt(KEY_PINS, lock.pins);
		nbt.putInt(KEY_STRENGTH, lock.strength);
		nbt.putBoolean(KEY_STURDY, lock.sturdy);
		
		if(lock.owner != null)
			nbt.putUUID(KEY_OWNER, lock.owner);
		
		if(!lock.trustedPlayers.isEmpty())
		{
			int i = 0;
			for(UUID uuid : lock.trustedPlayers)
			{
				nbt.putUUID(KEY_TRUSTED + i, uuid);
				i++;
			}
		}
		
		return nbt;
	}

	public static Lock fromBuf(FriendlyByteBuf buf)
	{
		int id = buf.readInt();
		int length = (int) buf.readByte();
		boolean locked = buf.readBoolean();
		int pins = buf.readInt();
		int strength = buf.readInt();
		boolean sturdy = buf.readBoolean();
		UUID owner = buf.readBoolean() ? buf.readUUID() : null;
		
		int trustedCount = buf.readInt();
		Set<UUID> trustedPlayers = new HashSet<>();
		for(int i = 0; i < trustedCount; i++)
		{
			trustedPlayers.add(buf.readUUID());
		}
		
		return new Lock(id, length, locked, pins, strength, sturdy, owner, trustedPlayers);
	}

	public static void toBuf(FriendlyByteBuf buf, Lock lock)
	{
		buf.writeInt(lock.id);
		buf.writeByte((int) lock.getLength());
		buf.writeBoolean(lock.isLocked());
		buf.writeInt(lock.pins);
		buf.writeInt(lock.strength);
		buf.writeBoolean(lock.sturdy);
		buf.writeBoolean(lock.owner != null);
		if(lock.owner != null)
			buf.writeUUID(lock.owner);
		
		buf.writeInt(lock.trustedPlayers.size());
		for(UUID uuid : lock.trustedPlayers)
		{
			buf.writeUUID(uuid);
		}
	}

	public byte[] shuffle(int length)
	{
		byte[] combo = new byte[length];
		for(byte a = 0; a < length; ++a)
			combo[a] = a;
		LocksUtil.shuffle(combo, this.rng);
		return combo;
	}

	/*
	public byte[] inverse(byte[] combination)
	{
		byte[] lookup = new byte[combination.length];
		for(byte a = 0; a < combination.length; ++a)
			lookup[combination[a]] = a;
		return lookup;
	}
	*/

	public int getLength()
	{
		return this.combo.length;
	}

	public boolean isLocked()
	{
		return this.locked;
	}

	public void setLocked(boolean locked)
	{
		if(this.locked == locked)
			return;
		this.locked = locked;
		this.setChanged();
		this.notifyObservers();
	}

	public int getPin(int index)
	{
		return this.combo[index];
	}

	public boolean checkPin(int index, int pin)
	{
		return this.getPin(index) == pin;
	}
	
	// Advanced lockpicking getters and setters
	public int getPins()
	{
		return this.pins;
	}
	
	public void setPins(int pins)
	{
		this.pins = Math.min(Math.max(pins, 3), 7);
	}
	
	public int getStrength()
	{
		return this.strength;
	}
	
	public void setStrength(int strength)
	{
		this.strength = Math.min(Math.max(strength, 1), 5);
	}
	
	public boolean isSturdy()
	{
		return this.sturdy;
	}
	
	public void setSturdy(boolean sturdy)
	{
		this.sturdy = sturdy;
	}
	
	public UUID getOwner()
	{
		return this.owner;
	}
	
	public void setOwner(UUID owner)
	{
		this.owner = owner;
		this.setChanged();
		this.notifyObservers();
	}
	
	public Set<UUID> getTrustedPlayers()
	{
		return new HashSet<>(this.trustedPlayers);
	}
	
	public void addTrustedPlayer(UUID player)
	{
		this.trustedPlayers.add(player);
		this.setChanged();
		this.notifyObservers();
	}
	
	public void removeTrustedPlayer(UUID player)
	{
		this.trustedPlayers.remove(player);
		this.setChanged();
		this.notifyObservers();
	}
	
	public boolean isTrusted(UUID player)
	{
		return this.trustedPlayers.contains(player);
	}
	
	public boolean isOwner(UUID player)
	{
		return this.owner != null && this.owner.equals(player);
	}
	
	/**
	 * Calculate lockpicking difficulty based on lock attributes
	 * Formula: (pins * strength) + (sturdy ? 25 : 0)
	 * @return The difficulty score for lockpicking this lock
	 */
	public int getLockpickDifficulty()
	{
		return (this.pins * this.strength) + (this.sturdy ? 25 : 0);
	}
	
	/**
	 * Calculate the chance of lockpick breaking based on difficulty
	 * @param baseLockpickChance Base success chance (from config)
	 * @return The chance (0.0 to 1.0) that the lockpick will break
	 */
	public float getLockpickBreakChance(float baseLockpickChance)
	{
		int difficulty = getLockpickDifficulty();
		// Higher difficulty = higher break chance
		// Max difficulty (7 pins * 5 strength + 25 sturdy) = 60
		// This gives a break chance of 0% at difficulty 0, scaling up to ~60% at max difficulty
		return Math.min(1.0f, difficulty / 100.0f);
	}
	
	/**
	 * Calculate success chance for lockpicking
	 * @param baseLockpickChance Base success chance from config
	 * @param lockpickStrength Strength of the lockpick being used
	 * @return The chance (0.0 to 1.0) of successfully picking this lock
	 */
	public float getLockpickSuccessChance(float baseLockpickChance, float lockpickStrength)
	{
		int difficulty = getLockpickDifficulty();
		// Adjust success chance based on lockpick strength and lock difficulty
		float chance = baseLockpickChance * lockpickStrength - (difficulty / 100.0f);
		return Math.max(0.0f, Math.min(1.0f, chance));
	}
}