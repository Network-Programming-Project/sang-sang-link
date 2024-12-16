package model;

import lombok.*;

import java.time.LocalDateTime;

// 채팅방에 속해있는 메시지
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMessage {
    private Long id;

    // 메시지 내용
    private String content;

    // 메시지 보낸 시간
    private LocalDateTime sendAt;

    // 메시지 보낸 사용자 식별
    private Long userId;
}
