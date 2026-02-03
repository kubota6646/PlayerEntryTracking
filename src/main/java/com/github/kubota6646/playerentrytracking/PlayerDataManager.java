package com.github.kubota6646.playerentrytracking;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * プレイヤーのUUIDデータを管理するクラス
 */
public class PlayerDataManager {
    
    private final Plugin plugin;
    private final File dataFile;
    private Configuration data;
    private final Set<UUID> playerCache;
    private final ReentrantReadWriteLock lock;
    
    public PlayerDataManager(Plugin plugin) {
        this.plugin = plugin;
        this.playerCache = new HashSet<>();
        this.lock = new ReentrantReadWriteLock();
        
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
            // 初期化時は同期的に保存
            saveDataSync();
            plugin.getLogger().info("playerdata.ymlを作成しました");
        } else {
            // 既存のファイルを読み込む
            try {
                data = ConfigurationProvider.getProvider(YamlConfiguration.class).load(dataFile);
                plugin.getLogger().info("playerdata.ymlを読み込みました");
                
                // キャッシュにロード
                List<String> players = data.getStringList("players");
                if (players != null) {
                    for (String uuidString : players) {
                        try {
                            playerCache.add(UUID.fromString(uuidString));
                        } catch (IllegalArgumentException e) {
                            plugin.getLogger().warning("無効なUUID形式をスキップしました: " + uuidString);
                        }
                    }
                    plugin.getLogger().info(playerCache.size() + "件のプレイヤーデータを読み込みました");
                }
            } catch (IOException e) {
                plugin.getLogger().severe("playerdata.ymlの読み込みに失敗しました: " + e.getMessage());
                // エラーの場合は新しいConfigurationを作成
                data = new Configuration();
                data.set("players", new ArrayList<String>());
            }
        }
    }
    
    /**
     * データファイルを保存する（同期）
     */
    private void saveDataSync() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(data, dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("playerdata.ymlの保存に失敗しました: " + e.getMessage());
        }
    }
    
    /**
     * データファイルを保存する（非同期）
     */
    private void saveData() {
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            lock.writeLock().lock();
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(data, dataFile);
            } catch (IOException e) {
                plugin.getLogger().severe("playerdata.ymlの保存に失敗しました: " + e.getMessage());
            } finally {
                lock.writeLock().unlock();
            }
        });
    }
    
    /**
     * プレイヤーが初回参加かどうかをチェックし、記録する
     * @param uuid プレイヤーのUUID
     * @return 初回参加の場合true
     */
    public boolean checkAndRecordPlayer(UUID uuid) {
        // 最初に読み取りロックで確認
        lock.readLock().lock();
        boolean isFirstJoin = !playerCache.contains(uuid);
        lock.readLock().unlock();
        
        if (isFirstJoin) {
            // 書き込みロックを取得
            lock.writeLock().lock();
            try {
                // ダブルチェック：書き込みロック取得後に再度確認
                if (!playerCache.contains(uuid)) {
                    // 初回参加の場合、キャッシュとデータに追加
                    playerCache.add(uuid);
                    
                    List<String> players = data.getStringList("players");
                    if (players == null) {
                        players = new ArrayList<>();
                    }
                    players.add(uuid.toString());
                    data.set("players", players);
                    
                    // 非同期で保存
                    saveData();
                    plugin.getLogger().info("プレイヤー " + uuid.toString() + " を記録しました");
                    
                    return true;
                }
                // 他のスレッドが既に追加していた場合
                return false;
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        return false;
    }
}
