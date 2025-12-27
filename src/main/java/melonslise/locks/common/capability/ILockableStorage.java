package melonslise.locks.common.capability;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import melonslise.locks.common.util.Lockable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface ILockableStorage extends INBTSerializable<ListTag>
{
	Int2ObjectMap<Lockable> get();

	void add(Lockable lkb);

	void remove(int id);
}