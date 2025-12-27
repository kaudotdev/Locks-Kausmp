package melonslise.locks.common.network.toclient;

import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RemoveLockablePacket(int id) implements CustomPacketPayload {
    
    public static final Type<RemoveLockablePacket> TYPE = 
        new Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "remove_lockable"));

    public static final StreamCodec<FriendlyByteBuf, RemoveLockablePacket> STREAM_CODEC = 
        StreamCodec.of(
            (buf, packet) -> buf.writeInt(packet.id),
            buf -> new RemoveLockablePacket(buf.readInt())
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RemoveLockablePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                var handler = Minecraft.getInstance().level.getData(LocksAttachments.LOCKABLE_HANDLER);
                if (handler != null) {
                    handler.remove(packet.id);
                }
            }
        });
    }
}