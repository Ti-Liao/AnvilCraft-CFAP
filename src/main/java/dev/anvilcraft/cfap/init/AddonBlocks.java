package dev.anvilcraft.cfap.init;

import dev.anvilcraft.cfap.block.FellingBlock;
import dev.anvilcraft.lib.v2.registrum.providers.RegistrumRecipeProvider;
import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.util.DataGenUtil;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static dev.anvilcraft.cfap.AnvilCraftCFAP.REGISTRUM;

public class AddonBlocks {
    static {
        REGISTRUM.defaultCreativeTab(AddonItemGroups.ADDON_ITEMS.getKey());
    }


    public static final BlockEntry<FellingBlock> FELLING_BLOCK = REGISTRUM.block("felling_block", FellingBlock::new)
            .simpleItem()
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                    .pattern("SA ")
                    .pattern("RCA")
                    .pattern("SA ")
                    .define('S', Items.COBBLESTONE)
                    .define('A', Items.AMETHYST_SHARD)
                    .define('C', Items.STONECUTTER)
                    .define('R', Items.REDSTONE)
                    .unlockedBy("has_cobblestone", RegistrumRecipeProvider.has(Items.COBBLESTONE))
                    .save(provider)
            )
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(properties -> properties.noOcclusion().isValidSpawn(Blocks::never))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .blockstate(DataGenUtil::noExtraModelOrState)

            .register();


    public static void register() {
    }
}
