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
    private volatile Long id;
    private String email;
    private String userName;
    private String password;
    private String port;
    private List<User> friends;
    private volatile List<ChatRoom> chatRooms;

    public void addChatRoom(ChatRoom chatRoom) {
        chatRooms.add(chatRoom);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", port=" + port +
                ", friends=" + friends.stream().map(User::getId).toList() + // 친구의 ID만 출력
                ", chatRooms=" + chatRooms.stream().map(ChatRoom::getId).toList() + // 채팅방의 ID만 출력
                '}';
    }
}
