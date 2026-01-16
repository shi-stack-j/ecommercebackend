package com.shivam.aiecommercebackend.utility;


import com.shivam.aiecommercebackend.entity.OrderEn;
import com.shivam.aiecommercebackend.enums.OrderStatus;
import com.shivam.aiecommercebackend.enums.PaymentMode;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {
    public static Specification<OrderEn> buildFilter(
            PaymentMode mode,
            LocalDate fromDate,
            LocalDate toDate,
            OrderStatus orderStatus,
            BigDecimal minimumAmount,
            BigDecimal maxAmount
    ){
        return (root,query,cp)->{
            List<Predicate> predicates=new ArrayList<>();
            if(mode!=null){
                predicates.add(
                        cp.equal(root.get("paymentMode"),mode)
                );
            }
            if(fromDate!=null){
                predicates.add(
                        cp.greaterThanOrEqualTo(root.get("createdAt"),fromDate.atStartOfDay())
                );
            }
            if(orderStatus!=null){
                predicates.add(
                        cp.equal(root.get("orderStatus"),orderStatus)
                );
            }
            if(toDate!=null){
                predicates.add(
                        cp.lessThanOrEqualTo(root.get("createdAt"),toDate.plusDays(1).atStartOfDay())
                );
            }
            if(minimumAmount!=null){
                predicates.add(
                        cp.greaterThanOrEqualTo(root.get("totalAmount"),minimumAmount)
                );
            }
            if(maxAmount!=null){
                predicates.add(
                        cp.lessThanOrEqualTo(root.get("totalAmount"),maxAmount)
                );
            }
            return cp.and(predicates.toArray(new Predicate[0]));
        };
    }
}

