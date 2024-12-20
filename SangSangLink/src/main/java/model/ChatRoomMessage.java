package model;

import lombok.*;

import java.time.LocalDateTime;

// 채팅방에 속해있는 메시지
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// TODO 작성한 텍스트가 한국어인지 영어인지 분별해서 저장하는 코드 추가 필요
public class ChatRoomMessage { // ChatRoom과 User의 매핑 테이블
    private Long id;

    private String content;

    private LocalDateTime sendAt;

    private Long userId;

    private Long chatRoomId;

    // KO or EN-US
    private String language;
}
