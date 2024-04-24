package com.switchwon.switchwonapi.common.exception;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    private ErrorCode errorCode;

    private String errorMessage;
}
