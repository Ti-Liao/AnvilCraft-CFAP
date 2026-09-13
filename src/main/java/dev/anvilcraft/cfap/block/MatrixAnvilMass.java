package dev.anvilcraft.cfap.block;

import dev.dubhe.anvilcraft.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MatrixAnvilMass extends MatrixAnvil {
    public MatrixAnvilMass(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {

        if (random.nextInt(4) == 0) {
            if (state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH) {
                level.addParticle(ModParticles.ANVILON_MASS.get(), pos.getX() + (random.nextDouble()) / 1.5 + 0.2, pos.getY() + 1.5, pos.getZ() + (random.nextDouble()), 0, -0.5, 0);
            } else {
                level.addParticle(ModParticles.ANVILON_MASS.get(), pos.getX() + (random.nextDouble()), pos.getY() + 1.5, pos.getZ() + (random.nextDouble()) / 1.5 + 0.2, 0, -0.5, 0);

            }

        }
    }
}
