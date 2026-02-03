package com.github.kubota6646.playerentrytracking;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * PlayerEntryTracking - BungeeCordプラグイン
 * プレイヤーの参加、サーバー移動、退出を全員に通知します
 */
public class PlayerEntryTracking extends Plugin {
    
    private PluginConfig pluginConfig;
    private PlayerDataManager playerDataManager;
    
    @Override
    public void onEnable() {
        // プラグイン起動時の処理
        getLogger().info("PlayerEntryTracking プラグインが有効になりました");
        
        // 設定ファイルを読み込む
        pluginConfig = new PluginConfig(this);
        
        // プレイヤーデータマネージャーを初期化
        playerDataManager = new PlayerDataManager(this);
        
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
     * プラグイン設定を取得
     * @return プラグイン設定
     */
    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }
    
    /**
     * プレイヤーデータマネージャーを取得
     * @return プレイヤーデータマネージャー
     */
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
    
    /**
     * 全プレイヤーにメッセージを送信する
     * @param message 送信するメッセージ（カラーコード変換済み）
     */
    public void broadcastMessage(String message) {
        TextComponent textComponent = new TextComponent(message);
        getProxy().broadcast(textComponent);
    }
}
