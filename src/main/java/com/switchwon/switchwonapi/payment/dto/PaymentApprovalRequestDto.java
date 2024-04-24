package com.switchwon.switchwonapi.payment.dto;

import com.switchwon.switchwonapi.payment.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentApprovalRequestDto {
    @Schema(description = "유저 ID")
    private String userId;

    @Schema(description = "지불금액")
    private Double amount;

    @Schema(description = "지불화폐")
    private Currency currency;

    @Schema(description = "상점 ID")
    private String merchantId;

    @Schema(description = "추가 결제타입")
    private String paymentMethod;

    @Schema(description = "추가 결제타입 상세정보")
    private PaymentDetailDto paymentDetails;
}
