package com.shivam.aiecommercebackend.utility;

import com.shivam.aiecommercebackend.dto.returnDtos.ReturnRequestDto;
import com.shivam.aiecommercebackend.entity.OrderItem;
import com.shivam.aiecommercebackend.entity.ReturnEn;
import com.shivam.aiecommercebackend.entity.UserDetailsPrincipal;
import com.shivam.aiecommercebackend.entity.UserEn;
import com.shivam.aiecommercebackend.enums.OrderStatus;
import com.shivam.aiecommercebackend.exception.ResourceNotFoundException;
import com.shivam.aiecommercebackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auth")
public class CustomAuthorization {
    @Autowired
    private ReturnRepo returnRepo;
    @Autowired
    private ReviewEnRepo reviewRepo;
    @Autowired
    private OrderEnRepo orderRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private UserEnRepo userEnRepo;
    public boolean isOwner(Long orderId){
        if(orderId==null || orderId<=0) return false;
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !(authentication.getPrincipal() instanceof UserDetailsPrincipal udp)){
            return false;
        }

        return orderRepo.findById(orderId)
                .map(order->order.getUserEn()
                        .getId().equals(udp.getId()))
                .orElse(false);
    }

    public boolean isAdminOrManager(){
        boolean isAdminManager=SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
        return isAdminManager;
    }

    public boolean canAccessOrder(Long orderID){
        return isAdminOrManager() || isOwner(orderID);
    }

    public boolean canRaiseRequest(ReturnRequestDto requestDto){
        OrderItem orderItem=orderItemRepo.findById(requestDto.getOrderItemId())
                .orElseThrow(()->new ResourceNotFoundException("OrderItem not found with the given id :- "+requestDto.getOrderItemId()));
        UserDetailsPrincipal principal=(UserDetailsPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId=principal.getId();
        boolean canRaise=orderItem.getOrderEn().getUserEn().getId().equals(userId);
        boolean isManagerAdmin=SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(e->e.getAuthority().equals("ROLE_ADMIN") || e.getAuthority().equals("ROLE_MANAGER"));
        return canRaise || isManagerAdmin;
    }

    public boolean canRaise(ReturnRequestDto requestDto){
        if (requestDto==null || requestDto.getOrderItemId()==null)return false;
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated()) return false;
        boolean isAdminManager=authentication
                .getAuthorities()
                .stream()
                .anyMatch(e->e
                        .getAuthority()
                        .equals("ROLE_ADMIN") ||
                        e.getAuthority().equals("ROLE_MANAGER"));
        if (isAdminManager)return true;

        Object principal=authentication.getPrincipal();
        if( ! (principal instanceof UserDetailsPrincipal upd)){
            return false;
        }
        return orderItemRepo.findById(requestDto.getOrderItemId())
                .map(item->
                        item
                                .getOrderEn()
                                .getUserEn()
                                .getId()
                                .equals(upd.getId())
                )
                .orElse(false);

    }

    public boolean canMakeReview(Long productId) {

        if (productId == null) return false;

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated())
            return false;

        // 1️⃣ ADMIN / MANAGER shortcut
        boolean isAdminManager =
                auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(r ->
                                r.equals("ROLE_ADMIN") || r.equals("ROLE_MANAGER")
                        );

        if (isAdminManager) return true;

        // 2️⃣ Logged-in user
        if (!(auth.getPrincipal() instanceof UserDetailsPrincipal udp))
            return false;

        Long currentUserId = udp.getId();

        // 3️⃣ Check: user purchased this product & delivered
        return orderItemRepo.existsByOrderEn_UserEn_IdAndProductEn_IdAndOrderEn_OrderStatus(
                currentUserId,
                productId,
                OrderStatus.DELIVERED
        );
    }

    public boolean canUpdateReview(Long reviewId) {

        if (reviewId == null) return false;

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated())
            return false;

        // 1️⃣ ADMIN / MANAGER shortcut
        boolean isAdminManager =
                auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(r ->
                                r.equals("ROLE_ADMIN") || r.equals("ROLE_MANAGER")
                        );

        if (isAdminManager) return true;

        // 2️⃣ Logged-in user
        if (!(auth.getPrincipal() instanceof UserDetailsPrincipal udp))
            return false;

        Long currentUserId = udp.getId();

        // 3️⃣ Check review ownership
        return reviewRepo.findById(reviewId)
                .map(review ->
                        review.getUserEn()
                                .getId()
                                .equals(currentUserId)
                )
                .orElse(false);
    }

    public boolean canSeeReturnRequest(Long returnID){
        if(returnID==null || returnID<=0)return false;
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminManager=authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r->
                        r.equals("ROLE_ADMIN") || r.equals("ROLE_MANAGER")
                );

        if(isAdminManager)return true;
        if(!(authentication.getPrincipal() instanceof UserDetailsPrincipal udp))return false;
        return returnRepo.findById(returnID)
                .map(r->
                        r.getUserEn()
                                .getId()
                                .equals(udp.getId())
                )
                .orElse(false);

    }
}
