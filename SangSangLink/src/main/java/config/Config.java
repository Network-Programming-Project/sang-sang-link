package config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./") // .env 파일 위치 지정
            .load();

    public static String get(String key) {
        return dotenv.get(key);
    }
}
