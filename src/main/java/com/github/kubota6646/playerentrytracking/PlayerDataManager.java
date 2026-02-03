package com.github.kubota6646.playerentrytracking;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * プレイヤーのUUIDデータを管理するクラス
 */
public class PlayerDataManager {
    
    private final Plugin plugin;
    private final File dataFile;
    private Configuration data;
    
    public PlayerDataManager(Plugin plugin) {
        this.plugin = plugin;
        
        // プラグインのデータフォルダを作成
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        
        this.dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        loadData();
    }
    
    /**
     * データファイルを読み込む
     */
    private void loadData() {
        if (!dataFile.exists()) {
            // ファイルが存在しない場合は新規作成
            data = new Configuration();
            data.set("players", new ArrayList<String>());
            saveData();
            plugin.getLogger().info("playerdata.ymlを作成しました");
        } else {
            // 既存のファイルを読み込む
            try {
                data = ConfigurationProvider.getProvider(YamlConfiguration.class).load(dataFile);
                plugin.getLogger().info("playerdata.ymlを読み込みました");
            } catch (IOException e) {
                plugin.getLogger().severe("playerdata.ymlの読み込みに失敗しました: " + e.getMessage());
                // エラーの場合は新しいConfigurationを作成
                data = new Configuration();
                data.set("players", new ArrayList<String>());
            }
        }
    }
    
    /**
     * データファイルを保存する
     */
    private void saveData() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(data, dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("playerdata.ymlの保存に失敗しました: " + e.getMessage());
        }
    }
    
    /**
     * プレイヤーが初回参加かどうかをチェック
     * @param uuid プレイヤーのUUID
     * @return 初回参加の場合true
     */
    public boolean isFirstJoin(UUID uuid) {
        List<String> players = data.getStringList("players");
        if (players == null) {
            players = new ArrayList<>();
        }
        return !players.contains(uuid.toString());
    }
    
    /**
     * プレイヤーのUUIDを記録する
     * @param uuid プレイヤーのUUID
     */
    public void recordPlayer(UUID uuid) {
        List<String> players = data.getStringList("players");
        if (players == null) {
            players = new ArrayList<>();
        }
        
        // 既に記録されている場合は何もしない
        if (players.contains(uuid.toString())) {
            return;
        }
        
        players.add(uuid.toString());
        data.set("players", players);
        saveData();
        plugin.getLogger().info("プレイヤー " + uuid.toString() + " を記録しました");
    }
}
