package com.allra.allrabackendtask.market.common.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorRes {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
}