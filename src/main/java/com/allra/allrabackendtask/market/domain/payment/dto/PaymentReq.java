package com.allra.allrabackendtask.market.domain.payment.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentReq {

    private String orderId;
    private Integer amount;
}