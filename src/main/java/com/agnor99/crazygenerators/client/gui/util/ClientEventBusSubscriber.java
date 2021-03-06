package com.agnor99.crazygenerators.client.gui.util;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.client.gui.*;
import com.agnor99.crazygenerators.init.ContainerInit;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CrazyGenerators.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {

        ScreenManager.registerFactory(ContainerInit.QUESTION_GENERATOR.get(), QuestionGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.TIMING_GENERATOR.get(), TimingGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.POSITION_GENERATOR.get(), PositionGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.ITEM_GENERATOR.get(), ItemGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.REDSTONE_GENERATOR.get(), RedstoneGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.STRUCTURE_GENERATOR.get(), StructureGeneratorScreen::new);
    }
}
