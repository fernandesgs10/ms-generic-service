package br.com.generic.service.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


public final class UserUtil {
    private static final String VALIDATION_REGEX = "^(?=.*\\d)(?=.*[@!#$%^&*()_+{}\\[\\]:;\"'<>,.?~\\\\/-])(?!.*\\s)[A-Za-z\\d@!#$%^&*()_+{}\\[\\]:;\"'<>,.?~\\\\/-]{5,10}$";

    private UserUtil() {}

    public static Date parseDate(Object dateObject) {
        if (dateObject == null) {
            return null;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return dateFormat.parse(dateObject.toString());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }

      public static boolean validateDateToken(Date dateCurrent, Date inputDate) {
        long diffInMillis = Math.abs(dateCurrent.getTime() - inputDate.getTime());
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        System.out.println("Diferença em minutos: " + diffInMinutes); // ou utilize um log
        return diffInMinutes > 15;
    }

    public static String convertMillisToHours(long millis) {
        long hours = millis / 3600000;
        long minutes = (millis % 3600000) / 60000;
        return hours + " hours";
    }

    public static boolean isValidPassword(String password) {
        return Pattern.matches(VALIDATION_REGEX, password);
    }
}
