package dev.anvilcraft.cfap.client;

import dev.anvilcraft.cfap.AnvilCraftCFAP;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = AnvilCraftCFAP.MOD_ID, dist = Dist.CLIENT)
public class AnvilCraftCFAPClient {
    public AnvilCraftCFAPClient(IEventBus modBus, ModContainer container) {
    }
}
