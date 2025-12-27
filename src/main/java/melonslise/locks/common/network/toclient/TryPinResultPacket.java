package melonslise.locks.common.network.toclient;

import melonslise.locks.Locks;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.init.LocksContainerTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TryPinResultPacket(boolean correct, boolean reset) implements CustomPacketPayload {
    
    public static final Type<TryPinResultPacket> TYPE = 
        new Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "try_pin_result"));

    public static final StreamCodec<FriendlyByteBuf, TryPinResultPacket> STREAM_CODEC = 
        StreamCodec.of(
            (buf, packet) -> {
                buf.writeBoolean(packet.correct);
                buf.writeBoolean(packet.reset);
            },
            buf -> new TryPinResultPacket(buf.readBoolean(), buf.readBoolean())
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TryPinResultPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            AbstractContainerMenu container = Minecraft.getInstance().player.containerMenu;
            if(container.getType() == LocksContainerTypes.LOCK_PICKING.get())
                ((LockPickingContainer) container).handlePin(packet.correct, packet.reset);
        });
    }
}