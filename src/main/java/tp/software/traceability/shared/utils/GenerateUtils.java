package tp.software.traceability.shared.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class GenerateUtils {
    private final Random RANDOM = new SecureRandom();
    private String ALPHANUMERIC = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private Long NUMERIC = 123456789L;



    public String generateStringUserId(int length) {
        return generateRandomString(length);
    }

    public Long generateNumericUserId(int length) {
        return generateRandomLong(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder();

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return new String(returnValue);
    }

    private Long generateRandomLong(int length) {
        StringBuilder returnValue = new StringBuilder();

        for (int i = 0; i < length; i++) {
            returnValue.append(NUMERIC.toString().charAt(RANDOM.nextInt(NUMERIC.toString().length())));
        }
        return Long.parseLong(new String(returnValue));
    }
}
