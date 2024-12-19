package db;

import model.User;

import java.util.ArrayList;
import java.util.List;

// 사용자 데이터베이스
public class UserDB {
    public static List<User> users = new ArrayList<>();
    private static Long autoIncrement = 0L;

    // 초기 데이터
    static {
        User user2 = User.builder()
                .email("angry7319@naver.com")
                .password("1234")
                .port("50001")
                .userName("jiwon")
                .friends(new ArrayList<>())
                .chatRooms(new ArrayList<>())
                .build();

        User user3 = User.builder()
                .email("email@naver.com")
                .password("1234")
                .port("50001")
                .userName("uwok")
                .friends(new ArrayList<>())
                .chatRooms(new ArrayList<>())
                .build();

        User user1 = User.builder()
                .email("hansung.ac.kr")
                .password("1234")
                .port("50001")
                .userName("bugi")
                .friends(List.of(user2, user3))
                .chatRooms(new ArrayList<>())
                .build();

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
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public static User getUserById(Long id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public static User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // 회원가입 메소드
    public static boolean register(User user) {
        // 이메일 중복 확인
        for (User existingUser : users) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                return false; // 중복 이메일인 경우 false 반환
            }
        }

        // 중복이 아니면 저장
        insert(user);
        return true; // 성공적으로 등록되었음을 알림
    }
}
