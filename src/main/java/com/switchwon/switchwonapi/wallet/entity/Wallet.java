package com.switchwon.switchwonapi.wallet.entity;

import com.switchwon.switchwonapi.payment.enums.Currency;
import com.switchwon.switchwonapi.support.jpa.BaseTimeEntity;
import com.switchwon.switchwonapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Wallet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Double balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
