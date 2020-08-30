package com.agnor99.crazygenerators.init;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.objects.container.PongGeneratorContainer;
import com.agnor99.crazygenerators.objects.container.PositionGeneratorContainer;
import com.agnor99.crazygenerators.objects.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.objects.container.TimingGeneratorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, CrazyGenerators.MOD_ID);

    public static final RegistryObject<ContainerType<QuestionGeneratorContainer>> QUESTION_GENERATOR = CONTAINER_TYPES.
            register("question_generator", () -> IForgeContainerType.create(QuestionGeneratorContainer::new));

    public static final RegistryObject<ContainerType<TimingGeneratorContainer>> TIMING_GENERATOR = CONTAINER_TYPES.
            register("timing_generator", () -> IForgeContainerType.create(TimingGeneratorContainer::new));

    public static final RegistryObject<ContainerType<PongGeneratorContainer>> PONG_GENERATOR = CONTAINER_TYPES.
            register("pong_generator", () -> IForgeContainerType.create(PongGeneratorContainer::new));

    public static final RegistryObject<ContainerType<PositionGeneratorContainer>> POSITION_GENERATOR = CONTAINER_TYPES.
            register("position_generator", () -> IForgeContainerType.create(PositionGeneratorContainer::new));
}

