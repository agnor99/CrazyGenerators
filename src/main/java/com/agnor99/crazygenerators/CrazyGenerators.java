package com.agnor99.crazygenerators;

import com.agnor99.crazygenerators.init.BlockInit;
import com.agnor99.crazygenerators.init.ContainerInit;
import com.agnor99.crazygenerators.init.ItemInit;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.NetworkUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(CrazyGenerators.MOD_ID)
public class CrazyGenerators {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "crazygenerators";
    public static CrazyGenerators instance;

    public CrazyGenerators() {
        instance = this;
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);

        TileInit.TILE_ENTITY_TYPES.register(modEventBus);
        ContainerInit.CONTAINER_TYPES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NetworkUtil.registerMessages();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }
    public static class GeneratorItemGroup extends ItemGroup {
        public static final GeneratorItemGroup instance = new GeneratorItemGroup(ItemGroup.GROUPS.length, "generatorTab");
        private GeneratorItemGroup(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack(BlockInit.question_generator);
        }

        @Override
        public void fill(NonNullList<ItemStack> itemStacks) {
            super.fill(itemStacks);
            itemStacks.sort((o1, o2) -> {
                //generator Core first, cause i want that
                if(o1.getItem().equals(ItemInit.generator_core)) {
                    return -1;
                }
                if(o2.getItem().equals(ItemInit.generator_core)) {
                    return 1;
                }
                return 0;
            });
        }
    }
}
