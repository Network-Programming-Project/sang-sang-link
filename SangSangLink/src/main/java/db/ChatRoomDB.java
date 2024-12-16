package db;

import model.ChatRoom;

import java.util.ArrayList;
import java.util.List;

// 채팅방 데이터베이스
public class ChatRoomDB {
    private static List<ChatRoom> chatRooms = new ArrayList<>();
    private static Long autoIncrement = 0L;

    // 테스토용 초기 데이터
    static {
        ChatRoom chatRoom = ChatRoom.builder()
                .title("샘플 데이터1")
                .users(new ArrayList<>())
                .messages(new ArrayList<>())
                .build()
                ;

        insert(chatRoom);
    }

    // 데이터베이스에 추가
    public static void insert(ChatRoom chatRoom) {
        chatRoom.setId(autoIncrement++);
        chatRooms.add(chatRoom);
    }

    public static List<ChatRoom> getChatRooms() {
        return chatRooms;
    }
}
