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
            // content의 첫 글자를 기반으로 언어 설정
            if (content != null && !content.isEmpty()) {
                char firstChar = content.charAt(0); // 첫 글자 가져오기
                if (Character.toString(firstChar).matches("[a-zA-Z]")) { // 영어인지 확인
                    this.language = "KO";
                } else if (Character.toString(firstChar).matches("[가-힣]")) { // 한글인지 확인
                    this.language = "EN-US";
                } else {
                    this.language = "UNKNOWN"; // 첫 글자가 영어/한글이 아니면 UNKNOWN
                }
            } else {
                this.language = "UNKNOWN"; // content가 비어 있거나 null인 경우
            }
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
