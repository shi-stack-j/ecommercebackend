package com.shivam.aiecommercebackend.enums;

public enum ReturnStatusEnum {
    REQUESTED,  //User ne return request raise ki
    APPROVED,   //Admin ne request approve ki
    REJECTED,   //Admin ne request reject ki
    INITIATED,  //Process start ho gaya
    COMPLETED  //Refund/Replacement complete ho gaya
}
