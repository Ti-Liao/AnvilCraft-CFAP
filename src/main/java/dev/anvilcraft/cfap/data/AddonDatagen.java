package dev.anvilcraft.cfap.data;

import dev.anvilcraft.cfap.AnvilCraftCFAP;
import dev.anvilcraft.cfap.data.lang.AddonLangHandler;
import dev.anvilcraft.cfap.data.recipe.AnvilCollisionCraftRecipeLoader;
import dev.anvilcraft.cfap.data.recipe.ProceduralProcessRecipeLoader;
import dev.anvilcraft.lib.v2.registrum.providers.ProviderType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.ibm.icu.impl.CurrencyData.provider;
import static dev.anvilcraft.cfap.AnvilCraftCFAP.REGISTRUM;

@EventBusSubscriber(modid = AnvilCraftCFAP.MOD_ID)
public class AddonDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

    }

    /**
     * 初始化生成器
     */
    public static void init() {
        REGISTRUM.addDataGenerator(ProviderType.LANG, AddonLangHandler::init);
        REGISTRUM.addDataGenerator(ProviderType.RECIPE, AnvilCollisionCraftRecipeLoader::init);
        REGISTRUM.addDataGenerator(ProviderType.RECIPE, ProceduralProcessRecipeLoader::init);

    }
}
