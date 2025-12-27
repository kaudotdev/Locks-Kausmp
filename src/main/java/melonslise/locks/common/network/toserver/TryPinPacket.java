package melonslise.locks.common.network.toserver;

import melonslise.locks.Locks;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.init.LocksContainerTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TryPinPacket(byte pin) implements CustomPacketPayload {
    
    public static final Type<TryPinPacket> TYPE = 
        new Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "try_pin"));

    public static final StreamCodec<FriendlyByteBuf, TryPinPacket> STREAM_CODEC = 
        StreamCodec.of(
            (buf, packet) -> buf.writeByte(packet.pin),
            buf -> new TryPinPacket(buf.readByte())
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TryPinPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var sender = context.player();
            if (sender != null) {
                AbstractContainerMenu container = sender.containerMenu;
                if(container.getType() == LocksContainerTypes.LOCK_PICKING.get())
                    ((LockPickingContainer) container).tryPin(packet.pin);
            }
        });
    }
}