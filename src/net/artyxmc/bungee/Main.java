package net.artyxmc.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.artyxmc.bungee.listeners.AuthManagerEvent;
import net.artyxmc.bungee.listeners.ReturnToLobby;
import net.artyxmc.bungee.objects.ArtyxAuthUser;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin{

    public static Main plugin;
    public static HashMap<String, ArtyxAuthUser> players = new HashMap<String, ArtyxAuthUser>();

    public void onEnable() {
        this.plugin = this;
        this.getProxy().getPluginManager().registerListener(this, new ReturnToLobby(this));
        BungeeCord.getInstance().registerChannel("Return");
        BungeeCord.getInstance().registerChannel("AutoLogin");
        BungeeCord.getInstance().registerChannel("Auth");
        this.getProxy().getPluginManager().registerListener((Plugin)this, (Listener)new AuthManagerEvent());
    }


    public void onLoad() {
    }

    private void sendMessageToBukkit(String channel, String message, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.sendData("Return", stream.toByteArray());
    }


    public static Main getPlugin() {
        return plugin;
    }


    public static void setPlugin(Main plugin) {
        Main.plugin = plugin;
    }




}
