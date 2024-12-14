package db;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB {

    static List<User> users = new ArrayList<User>();

    static {
        // 기본 데이터
        User user1=new User.UserBuilder()
                .email("hansung.ac.kr")
                .password("1234")
                .port("5001")
                .userName("bugi")
                .build()
                ;
        users.add(user1);
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static User getUser(String userName) {
        for (User user : users) {
            if(user.getUserName().equals(userName)){
                return user;
            }
        }
        return null;
    }

    public static User login(String email, String password){
        for (User user : users) {
            System.out.println("userDB"+user.getEmail());
            if(user.getEmail().equals(email) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }
}
