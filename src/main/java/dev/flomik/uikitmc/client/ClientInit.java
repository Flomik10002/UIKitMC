package dev.flomik.uikitmc.client;

import dev.flomik.uikitmc.UIKitMC;
import dev.flomik.uikitmc.client.screen.EditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UIKitMC.MODID, value = Dist.CLIENT)
public final class ClientInit {

    private ClientInit() {}

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent e) {
        UIKitKeybinds.register(e);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (UIKitKeybinds.OPEN_EDITOR.consumeClick()) {
            mc.setScreen(new EditorScreen());
        }
    }
}
