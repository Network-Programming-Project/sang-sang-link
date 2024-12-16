package session;

import model.User;

public class Session {
    static public User user;

    public static void setUser(User user){
        Session.user = user;
    }
}
