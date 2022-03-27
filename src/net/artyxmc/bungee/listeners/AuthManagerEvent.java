package net.artyxmc.bungee.listeners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import net.artyxmc.bungee.objects.ArtyxAuthUser;
import net.artyxmc.bungee.Main;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AuthManagerEvent implements Listener{

    @EventHandler
    public void onPlayerJoin(final PostLoginEvent e) {
        if(!Main.players.containsKey(e.getPlayer().getName())){
            Main.players.put(e.getPlayer().getName(), new ArtyxAuthUser(e.getPlayer().getName()));
        }else{
            Main.players.remove(e.getPlayer().getName());
            Main.players.put(e.getPlayer().getName(), new ArtyxAuthUser(e.getPlayer().getName()));
        }
    }
    @EventHandler
    public void onPlayerLeft(final PlayerDisconnectEvent e){
        if(Main.players.containsKey(e.getPlayer().getName())){
            Main.players.remove(e.getPlayer().getName());
        }
    }
    @EventHandler
    public void onPluginMessage(final PluginMessageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!event.getTag().equalsIgnoreCase("Auth")) {
            return;
        }
        if (!(event.getSender() instanceof Server)) {
            return;
        }
        event.setCancelled(true);
        try {
            final DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
            final String task = in.readUTF();
            if (!task.equals("PlayerLogin")) {
                return;
            }
            final String name = in.readUTF();
            final ArtyxAuthUser player = Main.players.get(name);
            player.setAuth(true);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void sendToAuth(String channel, String message, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.sendData("AutoLogin", stream.toByteArray());
    }
    @EventHandler
    public void onServer(ServerSwitchEvent e){
        if(Main.players.get(e.getPlayer().getName()).isAuth()){
            Map<String, ServerInfo> servers = BungeeCord.getInstance().getServers();
            for(String server : servers.keySet()){
                if(e.getPlayer().getServer().getInfo().getName().equalsIgnoreCase(servers.get(server).getName())){
                    sendToAuth("AutoLogin", e.getPlayer().getName(), servers.get(server));
                }
            }
        }
    }
    @EventHandler
    public void onServerSwitch(final ServerSwitchEvent event) {
        if(!event.getPlayer().getServer().getInfo().getName().contains("lobby")){
            if(!Main.players.get(event.getPlayer().getName()).isAuth()){
                final TextComponent kickReason = new TextComponent("Você precisa se autenticar primeiro.");
                kickReason.setColor(ChatColor.RED);
                event.getPlayer().disconnect((BaseComponent)kickReason);
            }
        }
    }
    @EventHandler(priority = -64)
    public void executecommand(ChatEvent e){
        if(e.isCommand()){
            if(e.getMessage().contains("register") || e.getMessage().contains("login") || e.getMessage().contains("logar") || e.getMessage().contains("registrar") || e.getMessage().contains("loginstaff")){
                return;
            }
            final ProxiedPlayer proxiedPlayer = (ProxiedPlayer)e.getSender();
            if(Main.players.get(proxiedPlayer.getName()).isAuth()){
            }else{
                e.setMessage("/");
                e.setCancelled(true);
                proxiedPlayer.sendMessage(ChatColor.RED+"Você não esta autenticado faça login para utilizar o chat.");
            }
        }
    }
}
