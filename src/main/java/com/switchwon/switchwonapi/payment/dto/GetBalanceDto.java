package com.switchwon.switchwonapi.payment.dto;

import com.switchwon.switchwonapi.payment.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetBalanceDto {
    @Schema(description = "유저 ID")
    private String userId;

    @Schema(description = "잔액")
    private Double balance;

    @Schema(description = "지불화폐")
    private Currency currency;
}
