package com.agnor99.crazygenerators.network.packets.item_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.ItemGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestItemPacket extends ClientPacket {


    public RequestItemPacket(BlockPos pos) {
        super(pos);
    }

    public RequestItemPacket(PacketBuffer buf) {
        super(buf);
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        GeneratorTileEntity gte = getTileEntity(context);
        if(gte instanceof ItemGeneratorTileEntity) {
            ItemGeneratorTileEntity igte = (ItemGeneratorTileEntity) gte;
            igte.generateItem();
        }
    }
}
