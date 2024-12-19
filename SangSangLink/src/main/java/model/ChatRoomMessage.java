package model;

import lombok.*;

import java.time.LocalDateTime;

// 채팅방에 속해있는 메시지
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMessage { // ChatRoom과 User의 매핑 테이블
    private Long id;

    // 메시지 내용
    private String content;

    // 메시지 보낸 시간
    private LocalDateTime sendAt;

    // 메시지 보낸 사용자 식별
    private Long userId;

    // 속해있는 채팅방
    private Long chatRoomId;
}
