package net.artyxmc.bungee.objects;

public class ArtyxAuthUser {

    private String playername;
    private boolean isAuth;

    public ArtyxAuthUser(String playername){
        this.playername = playername;
        isAuth = false;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }
}
