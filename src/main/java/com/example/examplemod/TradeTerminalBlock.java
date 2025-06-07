package com.example.examplemod;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import io.netty.buffer.Unpooled;

public class TradeTerminalBlock extends Block {
    public TradeTerminalBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.EMERALD).strength(4.0F));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, net.minecraft.core.BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            MenuProvider provider = new SimpleMenuProvider(
                (id, inv, plyr) -> new TradeTerminalMenu(id, inv, new net.minecraft.network.FriendlyByteBuf(Unpooled.buffer(0))),
                Component.literal("Trade Terminal")
            );
            player.openMenu(provider);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
