package com.allra.allrabackendtask.market.domain.payment.service;

import com.allra.allrabackendtask.market.domain.payment.dto.PaymentReq;
import com.allra.allrabackendtask.market.domain.payment.dto.PaymentRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.Duration;

@Slf4j
@Service
public class PaymentClient {

    private final WebClient webClient;

    @Value("${payment.api.url}")
    private String paymentApiUrl;

    public PaymentClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * 외부 결제 api 호출
     */
    public PaymentRes processPayment(PaymentReq request) {

        try {
            PaymentRes response = webClient.post()
                    .uri(paymentApiUrl)
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PaymentRes.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            log.info("========================= Payment Service ==========================");
            log.info("결제 api 답변 status: {}, transactionId: {}",
                    response.getStatus(), response.getTransactionId());

            return response;

        } catch (Exception e) {
            log.error("결제 api 실패 -> ", e);
            return new PaymentRes("FAILED", null, "결제 api 실패" + e.getMessage());
        }
    }
}
