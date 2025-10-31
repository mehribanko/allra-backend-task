package com.allra.allrabackendtask.market.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRes {

    private String status;          // "SUCCESS" / "FAILED"  <- https://app.beeceptor.com/console/allra-payment
    private String transactionId;
    private String message;
}