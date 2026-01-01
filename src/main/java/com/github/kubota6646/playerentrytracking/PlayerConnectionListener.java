package com.github.kubota6646.playerentrytracking;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * プレイヤーの接続イベントを監視するリスナー
 */
public class PlayerConnectionListener implements Listener {
    
    private final PlayerEntryTracking plugin;
    
    public PlayerConnectionListener(PlayerEntryTracking plugin) {
        this.plugin = plugin;
    }
    
    /**
     * プレイヤーがBungeeCordプロキシに接続した時
     */
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        PluginConfig config = plugin.getPluginConfig();
        
        // 参加通知が無効の場合は何もしない
        if (!config.isJoinNotificationEnabled()) {
            return;
        }
        
        ProxiedPlayer player = event.getPlayer();
        String message = config.getJoinMessage(player.getName());
        plugin.broadcastMessage(message);
        plugin.getLogger().info(player.getName() + "さんが参加しました");
    }
    
    /**
     * プレイヤーがサーバー間を移動した時
     */
    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        
        // 初回接続時（前のサーバーがnull）はメッセージを送信しない
        if (event.getFrom() == null) {
            return;
        }
        
        PluginConfig config = plugin.getPluginConfig();
        
        // サーバー移動通知が無効の場合は何もしない
        if (!config.isSwitchNotificationEnabled()) {
            return;
        }
        
        String serverName = player.getServer().getInfo().getName();
        String message = config.getSwitchMessage(player.getName(), serverName);
        plugin.broadcastMessage(message);
        plugin.getLogger().info(player.getName() + "さんが" + serverName + "サーバーへ移動しました");
    }
    
    /**
     * プレイヤーがBungeeCordプロキシから切断した時
     */
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PluginConfig config = plugin.getPluginConfig();
        
        // 退出通知が無効の場合は何もしない
        if (!config.isQuitNotificationEnabled()) {
            return;
        }
        
        ProxiedPlayer player = event.getPlayer();
        String message = config.getQuitMessage(player.getName());
        plugin.broadcastMessage(message);
        plugin.getLogger().info(player.getName() + "さんが退出しました");
    }
}
