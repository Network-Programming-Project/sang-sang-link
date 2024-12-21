package db;

import model.ChatRoom;
import model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 채팅방 데이터베이스
public class ChatRoomDB {
    private static List<ChatRoom> chatRooms = new ArrayList<>();
    private static Long autoIncrement = 0L;

    // 테스토용 초기 데이터
    // TODO User 객체의 chatRooms에도 채팅방 추가
    static {
        User user1=UserDB.getUserById(0L);
        User user2=UserDB.getUserById(1L);
        System.out.println(user1+ "기본 사용자 데이터 ChatRoom에 저장");

        ChatRoom chatRoom = ChatRoom.builder()
                .title("샘플 데이터1")
                .users(List.of(user1, user2))
                .messages(new ArrayList<>())
                .build()
                ;

        insert(chatRoom);

        user1.addChatRoom(chatRoom);
        user2.addChatRoom(chatRoom);

        ChatRoom chatRoom2 = ChatRoom.builder()
                .title("샘플 데이터1")
                .users(List.of(user1, user2))
                .messages(new ArrayList<>())
                .build()
                ;

        insert(chatRoom2);

        ChatRoom chatRoom3 = ChatRoom.builder()
                .title("샘플 데이터1")
                .users(List.of(user1, user2))
                .messages(new ArrayList<>())
                .build()
                ;

        insert(chatRoom3);

        user1.addChatRoom(chatRoom);
        user2.addChatRoom(chatRoom);

        user1.addChatRoom(chatRoom);
        user2.addChatRoom(chatRoom);

        System.out.println(chatRoom+"기본 채팅방 데이터 ChatRoomDB에 저장");
    }

    // 데이터베이스에 추가
    public static void insert(ChatRoom chatRoom) {
        chatRoom.setId(autoIncrement++);
        chatRooms.add(chatRoom);
    }

    // 로그인한 사용자가 들어가 있는 채팅방 보여주기
    public static List<ChatRoom> getChatRoomsByUserId(Long userId) {
        List<ChatRoom> userChatRooms = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            for(User user : chatRoom.getUsers()) {
                if(userId.equals(user.getId())) {
                    userChatRooms.add(chatRoom);
                }
            }
        }
        return userChatRooms;
    }

    // chatRoomId를 가지고 채팅방 객체 반환
    public static ChatRoom getChatRoomById(Long chatRoomId) {
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.getId().equals(chatRoomId)) {
                return chatRoom;
            }
        }
        return null;
    }
}
