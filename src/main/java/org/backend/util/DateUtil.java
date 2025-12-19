package org.backend.util;

import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private DateUtil() {}

    public static final String ISO_8601_GMT7_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static String getIso8601Time(){
        return getDateInFormat(ZonedDateTime.now(), ISO_8601_GMT7_FORMAT);
    }

    public static String getDateInFormat(ZonedDateTime zonedDateTime, String pattern) {
        if (zonedDateTime == null || !StringUtils.hasLength(pattern)) return null;

        return zonedDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
