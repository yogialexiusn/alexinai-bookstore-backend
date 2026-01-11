package org.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    @Value("${security.jwt.token.expiryMillis}")
    private static String expiryMillis;

    private DateUtil() {}

    public static final String ISO_8601_GMT7_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static String getIso8601Time(){
        return getDateInFormat(ZonedDateTime.now(), ISO_8601_GMT7_FORMAT);
    }

    public static String getDateInFormat(ZonedDateTime zonedDateTime, String pattern) {
        if (zonedDateTime == null || !StringUtils.hasLength(pattern)) return null;

        return zonedDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static Date getExpiryDate() {
        Date now = new Date();
        return new Date(now.getTime() + expiryMillis);
    }

    public static LocalDateTime getExpiryLocalDate() {
        return LocalDateTime.now().plus(Duration.ofMillis(Long.parseLong(expiryMillis)));

    }

}
