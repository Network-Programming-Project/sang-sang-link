package db;

import model.ChatRoomMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageDB {
    private static List<ChatRoomMessage> messages = new ArrayList<>();
    private static Long autoIncrement=0L;

    static{
        ChatRoomMessage chatRoomMessage1 = ChatRoomMessage.builder()
                .content("첫번째 내용")
                .chatRoomId(0L)
                .userId(0L)
                .build()
                ;
        insert(chatRoomMessage1);

        ChatRoomMessage chatRoomMessage2 = ChatRoomMessage.builder()
                .content("두번째 내용")
                .chatRoomId(0L)
                .userId(0L)
                .build()
                ;
        insert(chatRoomMessage2);

        ChatRoomMessage chatRoomMessage3 = ChatRoomMessage.builder()
                .content("세번째 내용")
                .chatRoomId(0L)
                .userId(1L)
                .build()
                ;
        insert(chatRoomMessage3);
    }

    public static void insert(ChatRoomMessage message) {
        message.setId(autoIncrement++);
        messages.add(message);
    }

    public static List<ChatRoomMessage> getMessages() {
        return messages;
    }
}
