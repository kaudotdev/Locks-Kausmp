package melonslise.locks.common.init;

import com.mojang.serialization.Codec;
import melonslise.locks.Locks;
import melonslise.locks.common.capability.LockableHandler;
import melonslise.locks.common.capability.LockableStorage;
import melonslise.locks.common.capability.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class LocksAttachments {
    
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = 
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Locks.ID);

    public static final Supplier<AttachmentType<LockableHandler>> LOCKABLE_HANDLER = 
        ATTACHMENT_TYPES.register("lockable_handler", () -> 
            AttachmentType.serializable(() -> new LockableHandler(null)).build()
        );

    public static final Supplier<AttachmentType<LockableStorage>> LOCKABLE_STORAGE = 
        ATTACHMENT_TYPES.register("lockable_storage", () -> 
            AttachmentType.serializable(() -> new LockableStorage(null)).build()
        );

    public static final Supplier<AttachmentType<Selection>> SELECTION = 
        ATTACHMENT_TYPES.register("selection", () -> 
            AttachmentType.serializable(Selection::new).build()
        );

    private LocksAttachments() {}

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
