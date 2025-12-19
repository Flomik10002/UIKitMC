package dev.flomik.uikitmc.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public final class UIKitKeybinds {
    public static final String CATEGORY = "key.categories.uikitmc";
    public static final KeyMapping OPEN_EDITOR = new KeyMapping(
            "key.uikitmc.open_editor",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_F10,
            CATEGORY
    );

    private UIKitKeybinds() {}

    public static void register(RegisterKeyMappingsEvent e) {
        e.register(OPEN_EDITOR);
    }
}