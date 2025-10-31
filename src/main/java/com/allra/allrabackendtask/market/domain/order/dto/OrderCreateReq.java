package com.allra.allrabackendtask.market.domain.order.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateReq  {

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "배송 주소 입력 바람.")
    private String deliveryAddress;
}