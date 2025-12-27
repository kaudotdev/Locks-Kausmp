package melonslise.locks.common.network.toclient;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import melonslise.locks.Locks;
import melonslise.locks.common.capability.ILockableHandler;
import melonslise.locks.common.capability.ILockableStorage;
import melonslise.locks.common.init.LocksAttachments;
import melonslise.locks.common.util.Lockable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AddLockableToChunkPacket(Lockable lockable, int x, int z) implements CustomPacketPayload {
    
    public AddLockableToChunkPacket(Lockable lkb, ChunkPos pos) {
        this(lkb, pos.x, pos.z);
    }

    public AddLockableToChunkPacket(Lockable lkb, LevelChunk ch) {
        this(lkb, ch.getPos());
    }
    
    public static final Type<AddLockableToChunkPacket> TYPE = 
        new Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "add_lockable_to_chunk"));

    public static final StreamCodec<FriendlyByteBuf, AddLockableToChunkPacket> STREAM_CODEC = 
        StreamCodec.of(
            (buf, packet) -> {
                Lockable.toBuf(buf, packet.lockable);
                buf.writeInt(packet.x);
                buf.writeInt(packet.z);
            },
            buf -> new AddLockableToChunkPacket(Lockable.fromBuf(buf), buf.readInt(), buf.readInt())
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AddLockableToChunkPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                ILockableStorage st = mc.level.getChunk(packet.x, packet.z).getData(LocksAttachments.LOCKABLE_STORAGE);
                ILockableHandler handler = mc.level.getData(LocksAttachments.LOCKABLE_HANDLER);
                if (st != null && handler != null) {
                    Int2ObjectMap<Lockable> lkbs = handler.getLoaded();
                    Lockable lkb = lkbs.get(packet.lockable.id);
                    if(lkb == lkbs.defaultReturnValue()) {
                        lkb = packet.lockable;
                        lkb.addObserver(handler);
                        lkbs.put(lkb.id, lkb);
                    }
                    st.add(lkb);
                }
            }
        });
    }
}