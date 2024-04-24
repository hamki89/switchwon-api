package com.switchwon.switchwonapi.payment.dto;

import com.switchwon.switchwonapi.payment.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentApprovalDto {
    @Schema(description = "결제 ID")
    private String paymentId;

    @Schema(description = "결제 상태")
    private String status;

    @Schema(description = "지불 총금액")
    private Double amountTotal;

    @Schema(description = "지불화폐")
    private Currency currency;

    @Schema(description = "결제시간")
    private LocalDateTime timestamp;
}
