package com.switchwon.switchwonapi.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetBalanceRequestDto {
    @Schema(description = "유저 ID")
    private String userId;
}
