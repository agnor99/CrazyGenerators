package com.agnor99.crazygenerators.objects.blocks;


import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class QuestionGeneratorBlock extends GeneratorBlock {
    public QuestionGeneratorBlock() {
        super("question_generator");
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileInit.QUESTION_GENERATOR.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(!world.isRemote) {
            TileEntity tile = world.getTileEntity(blockPos);
            if(tile instanceof QuestionGeneratorTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (QuestionGeneratorTileEntity)tile, blockPos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity  tileEntity = world.getTileEntity(blockPos);
            if(tileEntity instanceof QuestionGeneratorTileEntity) {
                InventoryHelper.dropItems(world, blockPos, ((QuestionGeneratorTileEntity) tileEntity).getItems());
            }
            tileEntity.remove();
        }
    }
}
