package model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

// 채팅방에 속해있는 메시지
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMessage { // ChatRoom과 User의 매핑 테이블
    private Long id;

    private String content;

    private LocalDateTime sendAt;

    private Long userId;

    private Long chatRoomId;

    // KO or EN-US
    private String language;

    // 외부에서 determine을 통해서만 언어 선택
    private void setLanguage(String language) {
        this.language = language;
    }

    // 언어 판별 로직
    // ex) 한국어이면 영어로 변환해야하기 때문에 영어로 세팅
    public void determineLanguage() {
        if (isKorean(content)) {
            this.language = "EN-US";
        } else if (isEnglish(content)) {
            this.language = "KO";
        } else {
            this.language = "UNKNOWN"; // 알 수 없는 언어
        }
    }

    // 문자열에 한글이 포함되어 있는지 확인
    private boolean isKorean(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        // 한글 유니코드 범위 (가~힣)
        Pattern koreanPattern = Pattern.compile("[가-힣]");
        return koreanPattern.matcher(text).find();
    }

    // 문자열이 영어로만 구성되어 있는지 확인
    private boolean isEnglish(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        // 알파벳과 공백으로만 구성
        Pattern englishPattern = Pattern.compile("^[a-zA-Z\\s]*$");
        return englishPattern.matcher(text).matches();
    }
}
