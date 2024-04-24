package com.switchwon.switchwonapi.payment.entity;

import com.switchwon.switchwonapi.payment.enums.Currency;
import com.switchwon.switchwonapi.support.jpa.BaseTimeEntity;
import com.switchwon.switchwonapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    private Double amount;

    private Double fees;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String merchantId;

    @Setter
    private String paymentMethod;

    @Setter
    private String paymentStatus;

    @Setter
    private LocalDateTime paymentCompleteTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
