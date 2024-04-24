package com.switchwon.switchwonapi.wallet.repository;

import com.switchwon.switchwonapi.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserId(String memberId);
}
