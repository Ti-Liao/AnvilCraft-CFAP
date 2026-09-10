package dev.anvilcraft.cfap.init;

import dev.anvilcraft.cfap.AnvilCraftCFAP;
import dev.anvilcraft.cfap.anvil.FellingBlockBehavior;
import dev.anvilcraft.cfap.block.FellingBlock;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.anvil.BlockDevourerBehavior;
import dev.dubhe.anvilcraft.api.event.AnvilBehaviorRegisterEvent;
import dev.dubhe.anvilcraft.block.BlockDevourerBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = AnvilCraftCFAP.MOD_ID)
public class AddonAnvilBehaviors {
    @SubscribeEvent
    public static void register(AnvilBehaviorRegisterEvent event){
        event.registerBehavior(
                state -> state.getBlock() instanceof FellingBlock && !state.getValue(BlockDevourerBlock.TRIGGERED),
                new FellingBlockBehavior()
        );

    }
}
