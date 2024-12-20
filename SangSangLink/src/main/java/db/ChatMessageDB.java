package db;

import model.ChatRoomMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageDB {
    private static List<ChatRoomMessage> messages = new ArrayList<>();
    public static Long autoIncrement=0L;

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

        ChatRoomMessage chatRoomMessage4 = ChatRoomMessage.builder()
                .content("네번째 내용")
                .chatRoomId(0L)
                .userId(1L)
                .build()
                ;
        insert(chatRoomMessage4);

        ChatRoomMessage chatRoomMessage5 = ChatRoomMessage.builder()
                .content("다번째 내용 다번째 내용 다번째 내용 다번째 내용")
                .chatRoomId(0L)
                .userId(1L)
                .build()
                ;
        insert(chatRoomMessage5);

        ChatRoomMessage chatRoomMessage6 = ChatRoomMessage.builder()
                .content("여섯번째 내용")
                .chatRoomId(0L)
                .userId(1L)
                .build()
                ;
        insert(chatRoomMessage6);

        ChatRoomMessage chatRoomMessage7 = ChatRoomMessage.builder()
                .content("일곱번째 내용")
                .chatRoomId(0L)
                .userId(1L)
                .build()
                ;
        insert(chatRoomMessage7);
    }

    public static void insert(ChatRoomMessage message) {

        message.setId(autoIncrement++);
        // 중복 메시지 확인: chatRoomId와 content가 모두 같은 경우
        for (ChatRoomMessage chatRoomMessage : messages) {
            if (chatRoomMessage.getChatRoomId().equals(message.getChatRoomId()) &&
                    chatRoomMessage.getContent().equals(message.getContent()) &&
                    chatRoomMessage.getUserId().equals(message.getUserId())) {
                return; // 중복 메시지이면 추가하지 않음
            }
        }
        messages.add(message);
    }

    public static List<ChatRoomMessage> getMessages(Long chatRoomId) {
        List<ChatRoomMessage> returnList = new ArrayList<>();
        for (ChatRoomMessage chatRoomMessage : messages) {
            if (chatRoomMessage.getChatRoomId().equals(chatRoomId)) {
                returnList.add(chatRoomMessage);
            }
        }
        return returnList;
    }
}
