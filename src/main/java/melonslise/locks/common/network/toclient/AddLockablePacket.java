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

public record AddLockablePacket(Lockable lockable) implements CustomPacketPayload {
    
    public static final Type<AddLockablePacket> TYPE = 
        new Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "add_lockable"));

    public static final StreamCodec<FriendlyByteBuf, AddLockablePacket> STREAM_CODEC = 
        StreamCodec.of(
            (buf, packet) -> Lockable.toBuf(buf, packet.lockable),
            buf -> new AddLockablePacket(Lockable.fromBuf(buf))
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AddLockablePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                var handler = Minecraft.getInstance().level.getData(LocksAttachments.LOCKABLE_HANDLER);
                if (handler != null) {
                    handler.add(packet.lockable);
                }
            }
        });
    }
}