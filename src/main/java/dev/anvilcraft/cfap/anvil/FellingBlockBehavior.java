package dev.anvilcraft.cfap.anvil;

import dev.anvilcraft.cfap.block.FellingBlock;
import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FellingBlockBehavior implements IAnvilBehavior {
    @Override
    public boolean handle(Level level, BlockPos hitBlockPos, BlockState hitBlockState, float fallDistance, AnvilEvent.OnLand event) {
        if (!(level instanceof ServerLevel serverLevel)) return false;
        FellingBlock block = (FellingBlock) hitBlockState.getBlock();
        int range=Math.min((int) fallDistance + 2, 4);
        level.setBlock(hitBlockPos, hitBlockState.setValue(FellingBlock.TRIGGERED, true), 2);
        block.felling(
                serverLevel,
                hitBlockPos,
                hitBlockState.getValue(FellingBlock.FACING),
                range,
                event.getEntity().getBlockState().getBlock()
        );
        level.scheduleTick(hitBlockPos, block, 4);
        return true;
    }
}
