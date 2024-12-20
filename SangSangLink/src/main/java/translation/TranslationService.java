package translation;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

public class TranslationService {
    // 싱글톤 패턴을 통해 구현
    private static TranslationService instance;

    public static TranslationService getInstance() {
        if (instance == null) {
            instance = new TranslationService();
        }
        return instance;
    }

    private String endPoint = "https://api-free.deepl.com/v2/translate";
    private String apiKey= Config.get("API_KEY");

    public TranslationResponse translate(String text, String targetLang) throws IOException {
        System.out.println("번역 기능 시작"+text+targetLang);
        TranslationRequest request = new TranslationRequest();
        request.setText(Collections.singletonList(text));
        request.setTargetLang(targetLang);

        // Jackson ObjectMapper를 사용하여 JSON 직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        // HttpURLConnection 설정
        URL url = new URL(endPoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "DeepL-Auth-Key " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // 요청 본문 전송
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes());
            os.flush();
        }

        // 응답 처리
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                // JSON 응답을 TranslationResponse 객체로 변환
                return objectMapper.readValue(response.toString(), TranslationResponse.class);
            }
        } else {
            throw new IOException("Translation API request failed with response code: " + responseCode);
        }
    }
}
