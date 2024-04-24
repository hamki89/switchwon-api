package com.switchwon.switchwonapi.payment;

import com.switchwon.switchwonapi.payment.dto.*;
import com.switchwon.switchwonapi.payment.service.GetBalanceService;
import com.switchwon.switchwonapi.payment.service.PaymentApprovalService;
import com.switchwon.switchwonapi.payment.service.PaymentEstimateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final GetBalanceService getBalanceService;
    private final PaymentEstimateService paymentEstimateService;
    private final PaymentApprovalService paymentApprovalService;

    @Operation(
            summary = "잔액 조회 API"
    )
    @GetMapping("/balance/{userId}")
    public GetBalanceDto getBalance(
            @PathVariable(value = "userId") String userId
    ) {
        return getBalanceService.run(GetBalanceRequestDto.builder()
                .userId(userId)
                .build());
    }

    @Operation(
            summary = "결제 예상 결과 조회 API"
    )
    @PostMapping("/estimate")
    public PaymentEstimateDto getEstimate(
            @RequestBody PaymentEstimateRequestDto request
    ) {
        return paymentEstimateService.run(request);
    }

    @Operation(
            summary = "결제 승인 요청 API"
    )
    @PostMapping("/approval")
    public PaymentApprovalDto paymentApproval(
            @RequestBody PaymentApprovalRequestDto request
    ) {
        return paymentApprovalService.run(request);
    }
}
