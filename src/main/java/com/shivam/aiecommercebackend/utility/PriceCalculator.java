package com.shivam.aiecommercebackend.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceCalculator {
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    public static BigDecimal calculateDiscountedPrice(
            BigDecimal price,
            BigDecimal discountPercentage
    ) {

        if (price == null || discountPercentage == null) {
            throw new IllegalArgumentException("Price and discount must not be null");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0 ||
                discountPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price and discount must be positive");
        }

        if (discountPercentage.compareTo(HUNDRED) > 0) {
            throw new IllegalArgumentException("Discount percentage cannot exceed 100%");
        }

        BigDecimal discountAmount = price
                .multiply(discountPercentage)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);

        return price.subtract(discountAmount);
    }
}
