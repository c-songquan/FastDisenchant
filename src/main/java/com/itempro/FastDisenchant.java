package com.itempro;

import com.itempro.config.ConfigHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class FastDisenchant implements ModInitializer {
    public static final String MOD_ID = "fastdisenchant";
    public static final String VERSION = "1.0.0";

    @Override
    public void onInitialize() {
        ConfigHandler.load();
        
        // Update Checker: Async request
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (ConfigHandler.ModEnabled.getBooleanValue()) {
                checkForUpdates(client);
            }
        });
    }

    private void checkForUpdates(MinecraftClient client) {
        CompletableFuture.runAsync(() -> {
            try {
                HttpClient httpClient = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://raw.githubusercontent.com/c-songquan/FastDisenchant/main/version.json"))
                        .build();
                
                String response = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
                // 簡單邏輯：如果回傳的版本號不同則提示
                if (response != null && !response.contains(VERSION)) {
                    client.execute(() -> {
                        if (client.player != null) {
                            client.player.sendMessage(Text.of("§a[FastDisenchant] §fNew version available on GitHub!"), false);
                        }
                    });
                }
            } catch (Exception ignored) {}
        });
    }
}