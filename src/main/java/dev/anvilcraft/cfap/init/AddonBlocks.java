package dev.anvilcraft.cfap.init;

import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;

import static dev.anvilcraft.cfap.AnvilCraftCFAP.REGISTRUM;

public class AddonBlocks {
    static {
        REGISTRUM.defaultCreativeTab(AddonItemGroups.ADDON_ITEMS.getKey());
    }

    public static final BlockEntry<Block> EXAMPLE_BLOCK = REGISTRUM
        .block("example_block", Block::new)
        .simpleItem()
        .register();

    public static void register() {
    }
}
