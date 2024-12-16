package model;

import jdk.jfr.DataAmount;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// 채팅방 정보를 가지는 객체
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    private Long id;

    // 채팅방 제목
    private String title;

    // 채팅방내 메시지들
    private List<ChatRoomMessage> messages;

    // 참여자
    private List<User> users;
}
