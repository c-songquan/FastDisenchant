package com.itempro.mixin;

import com.itempro.config.ConfigHandler;
import com.itempro.util.InventoryUtil;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrindstoneScreen.class)
public abstract class MixinGrindstoneScreen extends HandledScreen<GrindstoneScreenHandler> {
    private int tickCounter = 0;
    private boolean isAutomating = false;

    public MixinGrindstoneScreen(GrindstoneScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        if (!ConfigHandler.ModEnabled.getBooleanValue()) return;

        int x = this.x + 150 + ConfigHandler.ButtonOffsetX.getIntegerValue();
        int y = this.y + 10 + ConfigHandler.ButtonOffsetY.getIntegerValue();

        this.addDrawableChild(ButtonWidget.builder(Text.of("FD"), button -> {
            this.isAutomating = !this.isAutomating;
            if (this.isAutomating) {
                button.setMessage(Text.of("§aFD"));
            } else {
                button.setMessage(Text.of("FD"));
            }
            executeCycle();
        }).dimensions(x, y, 20, 20).build());
    }

    private void executeCycle() {
        if (client == null || client.player == null) return;

        // 檢查背包滿
        if (ConfigHandler.StopIfInventoryFull.getBooleanValue() && InventoryUtil.isMainInventoryFull(client.player)) {
            this.isAutomating = false;
            return;
        }

        // 1. 取出結果 (Slot 2)
        if (!this.handler.getSlot(InventoryUtil.GRINDSTONE_RESULT).getStack().isEmpty()) {
            if (ConfigHandler.AutoDrop.getBooleanValue()) {
                InventoryUtil.dropItem(client, InventoryUtil.GRINDSTONE_RESULT);
            } else {
                InventoryUtil.transferItem(client, InventoryUtil.GRINDSTONE_RESULT);
            }
            return;
        }

        // 2. 補貨 (從 Slot 3-29 尋找附魔物品)
        if (ConfigHandler.AutoRefill.getBooleanValue()) {
            for (int i = InventoryUtil.MAIN_INV_START; i <= InventoryUtil.MAIN_INV_END; i++) {
                if (InventoryUtil.hasEnchantments(this.handler.getSlot(i).getStack())) {
                    InventoryUtil.transferItem(client, i);
                    break; 
                }
            }
        }
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        if (this.isAutomating) {
            tickCounter++;
            if (tickCounter >= ConfigHandler.TickDelay.getIntegerValue()) {
                executeCycle();
                tickCounter = 0;
            }
        }
    }
}