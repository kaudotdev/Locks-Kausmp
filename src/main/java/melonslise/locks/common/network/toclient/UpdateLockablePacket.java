package melonslise.locks.common.network.toclient;

import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksAttachments;
import melonslise.locks.common.util.Lockable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateLockablePacket(int id, boolean locked) implements CustomPacketPayload {
    
    public UpdateLockablePacket(Lockable lkb) {
        this(lkb.id, lkb.lock.isLocked());
    }
    
    public static final Type<UpdateLockablePacket> TYPE = 
        new Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "update_lockable"));

    public static final StreamCodec<FriendlyByteBuf, UpdateLockablePacket> STREAM_CODEC = 
        StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.id);
                buf.writeBoolean(packet.locked);
            },
            buf -> new UpdateLockablePacket(buf.readInt(), buf.readBoolean())
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateLockablePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                var handler = Minecraft.getInstance().level.getData(LocksAttachments.LOCKABLE_HANDLER);
                if (handler != null) {
                    var lockable = handler.getLoaded().get(packet.id);
                    if (lockable != null && lockable.lock != null) {
                        lockable.lock.setLocked(packet.locked);
                    }
                }
            }
        });
    }
}