package com.shivam.aiecommercebackend.utility;

import com.shivam.aiecommercebackend.entity.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RefundCalculator {

    /**
     * Calculate refundable amount for a given order item
     *
     * @param orderItem    The OrderItem being returned
     * @param returnQty    Quantity to be returned (<= orderItem.quantity)
     * @param restockingFeePercent Restocking fee percentage (e.g., 5 for 5%)
     * @return Refundable amount
     */
    public static BigDecimal calculateRefundableAmount(OrderItem orderItem, int returnQty, BigDecimal restockingFeePercent) {
        if(orderItem == null) throw new IllegalArgumentException("OrderItem cannot be null");
        if(returnQty <= 0 || returnQty > orderItem.getQuantity())
            throw new IllegalArgumentException("Invalid return quantity");

        // 1️⃣ Base refund = price * return quantity
        BigDecimal baseRefund = orderItem.getPrice().multiply(BigDecimal.valueOf(returnQty));

        // 2️⃣ Apply restocking fee
        BigDecimal restockingFee = BigDecimal.ZERO;
        if(restockingFeePercent != null && restockingFeePercent.compareTo(BigDecimal.ZERO) > 0) {
            restockingFee = baseRefund.multiply(restockingFeePercent)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        // 3️⃣ Final refundable amount
        BigDecimal refundableAmount = baseRefund.subtract(restockingFee);

        // 4️⃣ Ensure non-negative
        if(refundableAmount.compareTo(BigDecimal.ZERO) < 0) {
            refundableAmount = BigDecimal.ZERO;
        }

        return refundableAmount.setScale(2, RoundingMode.HALF_UP);
    }
}

