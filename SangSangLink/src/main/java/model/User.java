package model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

// 사용자 정보를 담는 객체
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String userName;
    private String password;
    private String port;
    private List<User> friends;
    private List<ChatRoom> chatRooms;
}
