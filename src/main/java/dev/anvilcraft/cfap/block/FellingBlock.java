package dev.anvilcraft.cfap.block;

import com.mojang.serialization.MapCodec;

import dev.anvilcraft.lib.v2.util.Util;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.entity.fakeplayer.AnvilCraftFakePlayers;
import dev.dubhe.anvilcraft.api.hammer.HammerRotateBehavior;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import dev.dubhe.anvilcraft.api.itemhandler.ItemHandlerUtil;
import dev.dubhe.anvilcraft.block.BlockPlacerBlock;
import dev.dubhe.anvilcraft.block.PulseGeneratorBlock;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import dev.dubhe.anvilcraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static dev.dubhe.anvilcraft.event.giantanvil.shock.DestroyType.TRAVERSE_DEPTH;
import static dev.dubhe.anvilcraft.event.giantanvil.shock.DestroyType.VISIT_LIMIT;

public class FellingBlock extends HorizontalDirectionalBlock implements HammerRotateBehavior, IHammerRemovable {
    public static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 8, 16, 16, 16);
    public static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 0, 16, 16, 8);
    public static final VoxelShape WEST_SHAPE = Block.box(8, 0, 0, 16, 16, 16);
    public static final VoxelShape EAST_SHAPE = Block.box(0, 0, 0, 8, 16, 16);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final MapCodec<PulseGeneratorBlock> CODEC = simpleCodec(PulseGeneratorBlock::new);

    public FellingBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(TRIGGERED, false)
        );
    }



    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(TRIGGERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        Direction direction=context.getHorizontalDirection();
        if (player == null) {
            return this.defaultBlockState()
                    .setValue(FACING, direction.getOpposite());
        }
        if (player.isShiftKeyDown()) {
            return this.defaultBlockState()
                    .setValue(FACING, direction.getOpposite());
        } else {
            return this.defaultBlockState().setValue(FACING, direction);
        }
    }

    @Override
    protected void onPlace(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean movedByPiston
    ) {
        if (!level.isClientSide) {
            checkIfTriggered(level, state, pos);
        }
    }

    @Override
    public void tick(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {
        super.tick(state, level, pos, random);
        if (!state.getValue(TRIGGERED)) return;
        if (!BlockPlacerBlock.hasNeighborSignal(level, pos, state.getValue(FACING))) {
            level.setBlock(pos, state.setValue(TRIGGERED, false), 2);
        }
    }

    @Override
    public void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block neighborBlock,
            BlockPos neighborPos,
            boolean movedByPiston
    ) {
        if (!level.isClientSide) {
            checkIfTriggered(level, state, pos);
        }
    }

    private void checkIfTriggered(Level level, BlockState blockState, BlockPos blockPos) {
        boolean bl = blockState.getValue(TRIGGERED);
        if (bl != BlockPlacerBlock.hasNeighborSignal(level, blockPos, blockState.getValue(FACING))) {
            BlockState changedState = blockState.setValue(TRIGGERED, !bl);
            level.setBlock(blockPos, changedState, 2);
            if (!bl) {
                felling((ServerLevel) level, blockPos, blockState.getValue(FACING), 1);
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return switch (state.getValue(FACING)) {
            case UP, DOWN -> Shapes.empty();
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
        };
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    public void felling(ServerLevel level, BlockPos blockPos, Direction direction, int range) {
        this.felling(level, blockPos, direction, range, null);
    }

    /**
     * 破坏方块
     *
     * @param level             世界
     * @param blockPos       伐木器坐标
     * @param direction 破坏方向
     * @param range             破坏半径(正方形)
     * @param anvil             砸到伐木器的铁砧
     */
    @SuppressWarnings({"unreachable", "unused"})
    public void felling(
            ServerLevel level,
            BlockPos blockPos,
            Direction direction,
            int range,
            @Nullable Block anvil
    ) {
        BlockPos outputPos = blockPos.relative(direction.getOpposite());

        BlockPos devourCenterPos = blockPos.relative(direction, range);

        final List<IItemHandler> itemHandlerList = ItemHandlerUtil.getTargetItemHandlerList(
                outputPos,
                direction,
                level
        );

        Vec3 center = outputPos.getCenter();

        List<BlockPos> list = new ArrayList<>();
        for (int dx = -range; dx <= range; dx++) {
            for (int dz = -range; dz <= range; dz++) {
                BlockPos pos = devourCenterPos.offset(dx, 0, dz);
               list.add(pos);
            }
        }


        final List<BlockPos> filteredBlockPosList = new ArrayList<>();



        for (BlockPos pos : list) {
            level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM,new ItemStack(Items.AMETHYST_SHARD)),pos.getX()+1, pos.getY(), pos.getZ()+1,5,-1,0.25,-1,0.25);

            BlockState blockState = level.getBlockState(pos);
            if (blockState.isAir()) continue;
            if (isFellingApplicableBlock(blockState)) {
                BlockPos.breadthFirstTraversal(
                        pos,
                        TRAVERSE_DEPTH,
                        VISIT_LIMIT,
                        Util::acceptDirections,
                        it -> {
                            if (it.getY() < pos.getY()) return false;
                            BlockState state = level.getBlockState(it);
                            if (isFellingApplicableBlock(state)) {
                                devourSingleBlockInternalLogic(level, it, filteredBlockPosList, itemHandlerList, center);
                                return true;
                            }
                            return false;
                        }
                );
            }
        }



    }
    private static boolean isFellingApplicableBlock(BlockState blockState) {
        return blockState.is(ModBlockTags.FELLING_APPLICABLE);
    }
    //破坏
    private static void devourSingleBlockInternalLogic(
            ServerLevel level,  BlockPos devourBlockPos, List<BlockPos> filteredBlockPosList,
            @Nullable List<IItemHandler> itemHandlerList, Vec3 center
    ) {
        if (PistonMoveGuard.isReserved(level, devourBlockPos)) return;
        AABB aabb = new AABB(center.add(-0.125, -0.125, -0.125), center.add(0.125, 0.125, 0.125));
        final boolean insertEnabled = itemHandlerList != null && !itemHandlerList.isEmpty();
        final boolean dropOriginalPlace = !level.noCollision(aabb);

        if (filteredBlockPosList.contains(devourBlockPos)) return;
        BlockState devourBlockState = level.getBlockState(devourBlockPos);
        if (!DevourUtil.shouldDevour(devourBlockState)) return;
        if (AnvilCraft.CONFIG.blockDevourerProtectContainers
                && level.getCapability(Capabilities.ItemHandler.BLOCK, devourBlockPos, null) != null) {
            return;
        }
        BlockMiningEffect miningEffect = BlockMiningEffect.NORMAL;

        if (
                !miningEffect.isDisintegration()
                        && devourBlockState.is(ModBlockTags.BLOCK_DEVOURER_PROBABILITY_DROPPING)
                        && level.random.nextDouble() > 0.05
        ) {
            level.destroyBlock(devourBlockPos, false);
            return;
        }
        final List<ItemStack> dropList = BreakBlockUtil.drop(level, devourBlockPos, miningEffect);
        if (level.getBlockEntity(devourBlockPos) instanceof LecternBlockEntity lectern) {
            transferLecternContents(level, itemHandlerList, center, lectern, insertEnabled, dropOriginalPlace);
        }
        if (!(devourBlockState.getBlock() instanceof DoublePlantBlock)) {
            ServerPlayer player = AnvilCraftFakePlayers.getBlockPlacer().offerPlayer(level);
            try {
                devourBlockState.getBlock().playerWillDestroy(
                        level,
                        devourBlockPos,
                        devourBlockState,
                        player
                );
            } finally {
                AnvilCraftFakePlayers.getBlockPlacer().disable(player);
            }
        }

        level.destroyBlock(devourBlockPos, false);
        if (!miningEffect.isDisintegration()) {
            List<ItemStack> drops = new ArrayList<>(dropList);
            drops.addAll(collectItemDrops(level, devourBlockPos));
            for (ItemStack itemStack : drops) {
                if (insertEnabled) {
                    for (IItemHandler target : itemHandlerList) {
                        itemStack = ItemHandlerHelper.insertItemStacked(target, itemStack, false);
                    }
                }
                if (itemStack.isEmpty()) continue;
                if (dropOriginalPlace) {
                    Block.popResource(level, devourBlockPos, itemStack);
                } else {
                    AnvilUtil.dropItems(List.of(itemStack), level, center);
                }
            }
        }
        TriggerUtil.devourerDevourBlock(level, devourBlockPos, devourBlockState.getBlock());
    }

    /**
     * 转移讲台内容
     *
     * <p>虽然溜槽/漏斗无法与讲台交互，但吞噬器这类直接破坏的应该转移走才正常点</p>
     */
    private static void transferLecternContents(
            ServerLevel level,
            @Nullable List<IItemHandler> itemHandlerList,
            Vec3 center,
            LecternBlockEntity lectern,
            boolean insertEnabled,
            boolean dropOriginalPlace
    ) {
        ItemStack bookStack = lectern.getBook();
        if (insertEnabled) {
            assert itemHandlerList != null;
            for (IItemHandler target : itemHandlerList) {
                bookStack = ItemHandlerHelper.insertItem(target, bookStack, false);
                lectern.setBook(bookStack);
            }
        }
        if (!dropOriginalPlace) {
            AnvilUtil.dropItems(List.of(bookStack), level, center);
            lectern.setBook(ItemStack.EMPTY);
        }
    }

    private static List<ItemStack> collectItemDrops(ServerLevel level, BlockPos pos) {
        List<ItemStack> drops = new ArrayList<>();
        for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class, new AABB(pos))) {
            if (!itemEntity.blockPosition().equals(pos)) continue;
            ItemStack stack = itemEntity.getItem();
            if (!stack.isEmpty()) drops.add(stack);
            itemEntity.discard();
        }
        return drops;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }


}
