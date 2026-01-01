package com.github.kubota6646.playerentrytracking;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * PlayerEntryTracking - BungeeCordプラグイン
 * プレイヤーの参加、サーバー移動、退出を全員に通知します
 */
public class PlayerEntryTracking extends Plugin {
    
    @Override
    public void onEnable() {
        // プラグイン起動時の処理
        getLogger().info("PlayerEntryTracking プラグインが有効になりました");
        
        // イベントリスナーを登録
        getProxy().getPluginManager().registerListener(this, new PlayerConnectionListener(this));
        
        getLogger().info("イベントリスナーの登録が完了しました");
    }
    
    @Override
    public void onDisable() {
        // プラグイン停止時の処理
        getLogger().info("PlayerEntryTracking プラグインが無効になりました");
    }
    
    /**
     * 全プレイヤーにメッセージを送信する
     * @param message 送信するメッセージ
     */
    public void broadcastMessage(String message) {
        TextComponent textComponent = new TextComponent(ChatColor.YELLOW + message);
        getProxy().broadcast(textComponent);
    }
}
