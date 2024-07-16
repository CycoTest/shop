package com.personal.shop.component;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;
import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;
import java.io.StringReader;

@Component
@RequiredArgsConstructor
public class EnvironmentSetup implements ApplicationRunner {

    private final Environment springEnvironment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String activeProfile = springEnvironment.getActiveProfiles().length > 0 ?
                springEnvironment.getActiveProfiles()[0] : "dev";

        if ("prod".equals(activeProfile)) {
            loadEncryptedEnv();
        } else {
            loadDevEnv();
        }
    }

    private void loadDevEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .filename(".env.dev")
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
    }

    private void loadEncryptedEnv() throws Exception {
        String encryptionKey = System.getenv("ENV_ENCRYPTION_KEY");
        if (encryptionKey == null || encryptionKey.isEmpty()) {
            throw new IllegalStateException("ENV_ENCRYPTION_KEY is not set");
        }

        byte[] encryptedEnv = Files.readAllBytes(Paths.get("shop.env.enc"));
        byte[] decryptedEnv = decrypt(encryptedEnv, encryptionKey);

        String decryptedContent = new String(decryptedEnv);
        Properties properties = new Properties();
        properties.load(new StringReader(decryptedContent));

        properties.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
    }

    private byte[] decrypt(byte[] encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(Base64.getDecoder().decode(encryptedData));
    }
}