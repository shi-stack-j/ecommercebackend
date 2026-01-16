package com.shivam.aiecommercebackend.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TrackingNumberGenerator {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String generate() {

        String datePart = LocalDate.now().format(DATE_FORMAT);

        String randomPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();

        return "TRK-" + datePart + "-" + randomPart;
    }
}

