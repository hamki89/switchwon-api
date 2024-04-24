package com.switchwon.switchwonapi.payment.dto;

import com.switchwon.switchwonapi.payment.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEstimateDto {
    @Schema(description = "예상 지불총합금액")
    private Double estimatedTotal;

    @Schema(description = "결제수수료")
    private Double fees;

    @Schema(description = "지불화폐")
    private Currency currency;
}
