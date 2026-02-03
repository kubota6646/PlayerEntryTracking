package com.github.kubota6646.playerentrytracking;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * プラグインの設定を管理するクラス
 */
public class PluginConfig {
    
    private final Plugin plugin;
    private Configuration config;
    
    // デフォルトメッセージ
    private static final String DEFAULT_JOIN_MESSAGE = "&e{player}さんが参加しました";
    private static final String DEFAULT_FIRST_JOIN_MESSAGE = "&a{player}さんがサーバーに初参加しました！";
    private static final String DEFAULT_SWITCH_MESSAGE = "&e{player}さんが{server}サーバーへ移動しました";
    private static final String DEFAULT_QUIT_MESSAGE = "&e{player}さんが退出しました";
    
    public PluginConfig(Plugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    /**
     * 設定ファイルを読み込む
     */
    private void loadConfig() {
        // プラグインのデータフォルダを作成
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        
        // 設定ファイルが存在しない場合は、デフォルトをコピー
        if (!configFile.exists()) {
            try (InputStream in = plugin.getResourceAsStream("config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("デフォルトのconfig.ymlをコピーできませんでした: " + e.getMessage());
            }
        }
        
        // 設定ファイルを読み込む
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            plugin.getLogger().info("config.ymlを読み込みました");
        } catch (IOException e) {
            plugin.getLogger().severe("config.ymlの読み込みに失敗しました: " + e.getMessage());
            // デフォルト設定を使用
            config = new Configuration();
            // デフォルト値を設定
            config.set("messages.join", DEFAULT_JOIN_MESSAGE);
            config.set("messages.firstJoin", DEFAULT_FIRST_JOIN_MESSAGE);
            config.set("messages.switch", DEFAULT_SWITCH_MESSAGE);
            config.set("messages.quit", DEFAULT_QUIT_MESSAGE);
            config.set("notifications.join", true);
            config.set("notifications.switch", true);
            config.set("notifications.quit", true);
        }
    }
    
    /**
     * 設定を再読み込みする
     */
    public void reload() {
        loadConfig();
    }
    
    /**
     * メッセージを取得し、nullの場合はデフォルト値を返す
     * @param key 設定キー
     * @param defaultValue デフォルト値
     * @return メッセージ
     */
    private String getMessageOrDefault(String key, String defaultValue) {
        String message = config.getString(key, defaultValue);
        if (message == null) {
            message = defaultValue;
        }
        return message;
    }
    
    /**
     * 参加メッセージを取得
     * @param playerName プレイヤー名
     * @return フォーマットされたメッセージ
     */
    public String getJoinMessage(String playerName) {
        if (playerName == null) {
            playerName = "Unknown";
        }
        String message = getMessageOrDefault("messages.join", DEFAULT_JOIN_MESSAGE);
        return translateColorCodes(message.replace("{player}", playerName));
    }
    
    /**
     * 初参加メッセージを取得
     * @param playerName プレイヤー名
     * @return フォーマットされたメッセージ
     */
    public String getFirstJoinMessage(String playerName) {
        if (playerName == null) {
            playerName = "Unknown";
        }
        String message = getMessageOrDefault("messages.firstJoin", DEFAULT_FIRST_JOIN_MESSAGE);
        return translateColorCodes(message.replace("{player}", playerName));
    }
    
    /**
     * サーバー移動メッセージを取得
     * @param playerName プレイヤー名
     * @param serverName サーバー名
     * @return フォーマットされたメッセージ
     */
    public String getSwitchMessage(String playerName, String serverName) {
        if (playerName == null) {
            playerName = "Unknown";
        }
        if (serverName == null) {
            serverName = "Unknown";
        }
        String message = getMessageOrDefault("messages.switch", DEFAULT_SWITCH_MESSAGE);
        return translateColorCodes(message.replace("{player}", playerName).replace("{server}", serverName));
    }
    
    /**
     * 退出メッセージを取得
     * @param playerName プレイヤー名
     * @return フォーマットされたメッセージ
     */
    public String getQuitMessage(String playerName) {
        if (playerName == null) {
            playerName = "Unknown";
        }
        String message = getMessageOrDefault("messages.quit", DEFAULT_QUIT_MESSAGE);
        return translateColorCodes(message.replace("{player}", playerName));
    }
    
    /**
     * 参加通知が有効かどうか
     * @return 有効な場合true
     */
    public boolean isJoinNotificationEnabled() {
        return config.getBoolean("notifications.join", true);
    }
    
    /**
     * サーバー移動通知が有効かどうか
     * @return 有効な場合true
     */
    public boolean isSwitchNotificationEnabled() {
        return config.getBoolean("notifications.switch", true);
    }
    
    /**
     * 退出通知が有効かどうか
     * @return 有効な場合true
     */
    public boolean isQuitNotificationEnabled() {
        return config.getBoolean("notifications.quit", true);
    }
    
    /**
     * カラーコード(&)をChatColorに変換
     * @param message メッセージ
     * @return 変換されたメッセージ
     */
    private String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
