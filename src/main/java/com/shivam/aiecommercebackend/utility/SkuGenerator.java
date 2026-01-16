package com.shivam.aiecommercebackend.utility;

import java.util.UUID;

public class SkuGenerator {

    public static String generate(String productName, String categoryName) {

        String productPart = productName
                .trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", "")
                .substring(0, Math.min(5,
                        productName.replaceAll("[^A-Z0-9]", "").length()));

        String categoryPart = categoryName
                .trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", "")
                .substring(0, Math.min(3,
                        categoryName.replaceAll("[^A-Z0-9]", "").length()));

        String randomPart = UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

        return categoryPart + "-" + productPart + "-" + randomPart;
    }
}
