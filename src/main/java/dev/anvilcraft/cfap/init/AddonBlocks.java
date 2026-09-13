package dev.anvilcraft.cfap.init;

import dev.anvilcraft.cfap.block.FellingBlock;
import dev.anvilcraft.cfap.block.MatrixAnvil;
import dev.anvilcraft.cfap.block.MatrixAnvilMass;
import dev.anvilcraft.lib.v2.registrum.providers.RegistrumRecipeProvider;
import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntry;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.util.DataGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
public static final BlockEntry<MatrixAnvilMass> MATRIX_ANVIL_MASS = REGISTRUM.block("matrix_anvil_mass", MatrixAnvilMass::new)
        .initialProperties(() -> Blocks.ANVIL)
        .properties(properties -> properties.isValidSpawn(Blocks::never).strength(5.0f, 1200f))
        .blockstate(DataGenUtil::noExtraModelOrState)
        .item()
        .tag(ItemTags.ANVIL)
        .build()
        .properties(properties -> properties.noOcclusion().isValidSpawn(Blocks::never))
        .tag(BlockTags.ANVIL, ModBlockTags.CANT_BROKEN_ANVIL, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL,ModBlockTags.ANVIL_TIER_1)
        .properties(properties -> properties
                .lightLevel(state -> 9)
                .noOcclusion()
                .emissiveRendering(AddonBlocks::always)
        )
        .register();

    public static void register() {
    }

    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entity) {
        return false;
    }

    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    public static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }
}
