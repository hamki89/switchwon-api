package com.switchwon.switchwonapi.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailDto {
    @Schema(description = "카드번호")
    private String cardNumber;

    @Schema(description = "카드 유효기간")
    private String expiryDate;

    @Schema(description = "카드 CVV")
    private String cvv;
}
