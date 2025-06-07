package com.funcation.blocks;

import com.funcation.blocks.entity.TradeBlockEntity;
import com.funcation.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

/**
 * The Trade Block allows players to exchange early game resources
 * to reduce tedious mining/collecting activities while maintaining game balance.
 *
 * @since 1.0.0
 */
public class TradeBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    /**
     * Creates a new TradeBlock with appropriate block properties.
     * - Hardness: 3.5F (stone-like)
     * - Resistance: 6.0F (stone-like)
     * - Requires correct tool for drops (pickaxe)
     * - Sound: Stone
     * - Map color: Stone
     */
    public TradeBlock() {
        super(Properties.of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5F, 6.0F) // hardness, resistance
                .sound(SoundType.STONE));

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /* BLOCK ENTITY */

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TradeBlockEntity) {
                ((TradeBlockEntity) blockEntity).dropContents(level, pos);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        System.out.println("[TRADE DEBUG] TradeBlock.use called. isClientSide=" + level.isClientSide() + ", pos=" + pos + ", player=" + player.getName().getString());
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            System.out.println("[TRADE DEBUG] BlockEntity at pos: " + blockEntity);
            if (blockEntity instanceof TradeBlockEntity) {
                System.out.println("[TRADE DEBUG] Opening screen for player: " + player.getName().getString());
                NetworkHooks.openScreen((ServerPlayer) player, (TradeBlockEntity) blockEntity, pos);
            } else {
                System.out.println("[TRADE DEBUG] Container provider missing!");
                throw new IllegalStateException("Container provider missing!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TradeBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.TRADE_BLOCK_ENTITY.get(), TradeBlockEntity::tick);
    }
}
