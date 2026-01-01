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
        ProxiedPlayer player = event.getPlayer();
        String message = player.getName() + "さんが参加しました";
        plugin.broadcastMessage(message);
        plugin.getLogger().info(message);
    }
    
    /**
     * プレイヤーがサーバー間を移動した時
     */
    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        
        // 初回接続時（前のサーバーがnull）はメッセージを送信しない
        if (event.getFrom() != null) {
            String serverName = player.getServer().getInfo().getName();
            String message = player.getName() + "さんが" + serverName + "サーバーへ移動しました";
            plugin.broadcastMessage(message);
            plugin.getLogger().info(message);
        }
    }
    
    /**
     * プレイヤーがBungeeCordプロキシから切断した時
     */
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String message = player.getName() + "さんが退出しました";
        plugin.broadcastMessage(message);
        plugin.getLogger().info(message);
    }
}
