package db;

import model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 사용자 데이터베이스
public class UserDB {
    private static List<User> users = new ArrayList<User>();
    private static Long autoIncrement = 0L;

    // 초기 데이터
    static {
        // 기본 데이터 2
        User user2=User.builder()
                .email("angry7319@naver.com")
                .password("1234")
                .port("5001")
                .userName("jiwon")
                .friends(new ArrayList<>())
                .build()
                ;

        // 기본 데이터 3
        User user3=User.builder()
                .email("email@naver.com")
                .password("1234")
                .port("5001")
                .userName("uwok")
                .friends(new ArrayList<>())
                .build()
                ;

        // 기본 데이터
        User user1=User.builder()
                .email("hansung.ac.kr")
                .password("1234")
                .port("5001")
                .userName("bugi")
                .friends(List.of(user2, user3))
                .build()
                ;

        insert(user1);
        insert(user2);
        insert(user3);
    }

    // 데이터베이스에 저장
    public static void insert(User user) {
        user.setId(autoIncrement++);
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
