package com.itempro.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryUtil {
    // 嚴格限制：GrindstoneScreenHandler 中，3-29 對應玩家的 Slot 9-35 (主背包)
    public static final int MAIN_INV_START = 3;
    public static final int MAIN_INV_END = 29;
    public static final int GRINDSTONE_RESULT = 2;

    public static boolean hasEnchantments(ItemStack stack) {
        if (stack.isEmpty()) return false;
        // 1.21 檢查方式：DataComponentTypes.ENCHANTMENTS 或 STORED_ENCHANTMENTS (附魔書)
        return stack.getComponents().contains(DataComponentTypes.ENCHANTMENTS) || 
               stack.getComponents().contains(DataComponentTypes.STORED_ENCHANTMENTS);
    }

    public static void transferItem(MinecraftClient client, int slotId) {
        if (client.interactionManager == null || client.player == null) return;
        int syncId = client.player.currentScreenHandler.syncId;
        
        client.execute(() -> {
            if (client.player.currentScreenHandler instanceof GrindstoneScreenHandler) {
                client.interactionManager.clickSlot(syncId, slotId, 0, SlotActionType.QUICK_MOVE, client.player);
            }
        });
    }

    public static void dropItem(MinecraftClient client, int slotId) {
        if (client.interactionManager == null || client.player == null) return;
        int syncId = client.player.currentScreenHandler.syncId;
        
        client.execute(() -> {
            client.interactionManager.clickSlot(syncId, slotId, 1, SlotActionType.THROW, client.player);
        });
    }

    public static boolean isMainInventoryFull(PlayerEntity player) {
        // 僅檢查 Slot 9-35
        for (int i = 9; i <= 35; i++) {
            if (player.getInventory().getStack(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}