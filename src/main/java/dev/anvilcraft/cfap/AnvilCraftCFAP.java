package dev.anvilcraft.cfap;

import com.mojang.logging.LogUtils;
import dev.anvilcraft.cfap.data.AddonDatagen;
import dev.anvilcraft.cfap.init.AddonBlocks;
import dev.anvilcraft.cfap.init.AddonItemGroups;
import dev.anvilcraft.cfap.init.AddonItems;
import dev.anvilcraft.lib.v2.config.ConfigManager;
import dev.anvilcraft.lib.v2.registrum.Registrum;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(AnvilCraftCFAP.MOD_ID)
public class AnvilCraftCFAP {
    public static final String MOD_ID = "anvilcraft_cfap";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final AddonConfig CONFIG = ConfigManager.register(AnvilCraftCFAP.MOD_ID, AddonConfig::new);
    public static final Registrum REGISTRUM = Registrum.create(MOD_ID);

    public AnvilCraftCFAP(IEventBus modEventBus, ModContainer modContainer) {
        AddonItemGroups.register(modEventBus);
        AddonBlocks.register();
        AddonItems.register();
        AddonDatagen.init();
    }

    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
