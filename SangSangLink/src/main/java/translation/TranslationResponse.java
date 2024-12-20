package translation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class TranslationResponse {
    private List<Translation> translations;

    @Data
    public static class Translation {
        @JsonProperty("detected_source_language")
        private String detectedSourceLanguage;
        private String text;
    }
}

