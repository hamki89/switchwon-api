package com.switchwon.switchwonapi.payment.service;

import com.switchwon.switchwonapi.common.exception.SwitchwonException;
import com.switchwon.switchwonapi.payment.dto.GetBalanceDto;
import com.switchwon.switchwonapi.payment.dto.GetBalanceRequestDto;
import com.switchwon.switchwonapi.wallet.entity.Wallet;
import com.switchwon.switchwonapi.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.switchwon.switchwonapi.common.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class GetBalanceService {
    private final WalletRepository walletRepository;

    public GetBalanceDto run(GetBalanceRequestDto input) {
        Wallet wallet = walletRepository.findByUserId(input.getUserId()).orElseThrow(() -> {
            throw new SwitchwonException(NOT_FOUND_USER);
        });

        return GetBalanceDto.builder()
                .userId(wallet.getUser().getId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .build();
    }
}
