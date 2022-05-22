package com.parent.ws.app.shared;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generateRandomString(int length) {
        StringBuilder stringValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int charactersLength = CHARACTERS.length();
            int randomNumber = RANDOM.nextInt(charactersLength);

            char randomCharacter = CHARACTERS.charAt(randomNumber);
            stringValue.append(randomCharacter);
        }

        return new String(stringValue);

    }
}
