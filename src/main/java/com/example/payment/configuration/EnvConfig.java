package com.example.payment.configuration;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
@Component
public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();  // Use configure() method before load()

    public static String get(String key) {
        return dotenv.get(key);
    }
}