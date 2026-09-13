package dev.anvilcraft.cfap.data.recipe;

import dev.anvilcraft.cfap.init.AddonBlocks;
import dev.anvilcraft.lib.v2.registrum.providers.RegistrumRecipeProvider;
import dev.dubhe.anvilcraft.block.state.IrradiatorType;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.recipe.anvil.procedural.ProceduralProcessRecipeBuilder;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.BlockCompressRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.wrap.BlockProcessingRecipe;
import net.minecraft.world.level.block.Blocks;

public class ProceduralProcessRecipeLoader {
    public static void init(RegistrumRecipeProvider provider){
        //质量矩砧
        ProceduralProcessRecipeBuilder.of(AddonBlocks.MATRIX_ANVIL_MASS.get())
                .addStep(
                        BlockCompressRecipe.builder()
                                .input(Blocks.IRON_BLOCK)
                                .input(Blocks.IRON_BLOCK)
                                .result(ModBlocks.WIP_BLOCK.get())
                                .buildRecipe()
                )
                .addStep(
                        BlockCompressRecipe.builder()
                                .input(ModBlocks.HEAVY_IRON_BLOCK.get())
                                .input(ModBlocks.WIP_BLOCK.get())
                                .result(ModBlocks.WIP_BLOCK.get())
                                .buildRecipe()
                )
                .loop(3)
                .multipleLoopFirstStep(
                        BlockCompressRecipe.builder()
                                .input(Blocks.IRON_BLOCK)
                                .input(ModBlocks.WIP_BLOCK.get())
                                .result(ModBlocks.WIP_BLOCK.get())
                                .buildRecipe()
                )
                .result(AddonBlocks.MATRIX_ANVIL_MASS)
                .icon(AddonBlocks.MATRIX_ANVIL_MASS.asStack())
                .save(provider,"matrix_anvil_mass");

        ProceduralProcessRecipeBuilder.of(AddonBlocks.MATRIX_ANVIL_MASS.get())
                .addStep(
                        BlockCompressRecipe.builder()
                                .input(Blocks.ANVIL)
                                .input(ModBlocks.RESIN_BLOCK.get())
                                .result(ModBlocks.WIP_BLOCK.get())
                                .buildRecipe()
                )
                .loop(9)
                .multipleLoopFirstStep(
                        BlockCompressRecipe.builder()
                                .input(Blocks.ANVIL)
                                .input(ModBlocks.WIP_BLOCK.get())
                                .result(ModBlocks.WIP_BLOCK.get())
                                .buildRecipe()
                )
                .result(AddonBlocks.MATRIX_ANVIL_MASS)
                .icon(AddonBlocks.MATRIX_ANVIL_MASS.asStack())
                .save(provider,"matrix_anvil_mass_cheaper");



    }
}
