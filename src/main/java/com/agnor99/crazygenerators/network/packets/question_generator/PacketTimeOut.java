package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.client.gui.QuestionGeneratorScreen;
import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.other.generator.question.Question;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTimeOut implements ServerPacket {
    private final BlockPos pos;
    private final String question;
    private final String[] answers;
    public PacketTimeOut(PacketBuffer buf) {
        pos = buf.readBlockPos();
        question = buf.readString();
        answers = new String[4];
        for(int i = 0; i < 4; i++) {
            answers[i] = buf.readString();
        }
    }
    public PacketTimeOut(BlockPos pos, Question question) {
        this.pos = pos;
        this.question = question.getQuestion();
        answers = question.getAnswerPossibilities();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeString(question);
        for(int i = 0; i < 4; i++) {
            buf.writeString(answers[i]);
        }
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof QuestionGeneratorScreen) {
            QuestionGeneratorScreen qgScreen = (QuestionGeneratorScreen) screen;
            qgScreen.updateQuestion(question, answers);
        }
        context.get().setPacketHandled(true);
    }
}
