package net.artyxmc.bungee.listeners;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.artyxmc.bungee.Main;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ReturnToLobby implements Listener{

    public Main plugin;

    public ReturnToLobby(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onKick(final ServerKickEvent event) {
        final ProxiedPlayer p = event.getPlayer();
        if (event.getPlayer().getServer() != null) {
            if (!event.getPlayer().getServer().getInfo().getName().contains("lobby")) {
                if(event.getKickReason().contains("[ARTYX] Reiniciando servidor")) {
                    event.setCancelled(true);
                    plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                        @Override
                        public void run() {
                            p.sendMessage(new TextComponent(ChatColor.RED + "§cO §cservidor §cem §cque §cvocê §cestava §cconectado §cagora §cestá §creiniciando, §caguarde §calguns §cminutos §caté §cque §cele reinicie."));
                        }
                    }, 1l, TimeUnit.MICROSECONDS);
                }
            }
        }
    }

    public static String getRandomLobby() {
        Random r = new Random();
        ArrayList<String> u = new ArrayList<String>();
        BungeeCord.getInstance().getServers().entrySet().forEach(server->{
            if(server.getKey().contains("lobby")) {
                u.add(server.getKey());
            }
        });
        if(u.size() > 1) {
            return u.get(r.nextInt(u.size()));
        }else if(u.size() == 1){
            return u.get(0);
        }else {
            return "Nenhum lobby foi detectado!";
        }
    }
}
