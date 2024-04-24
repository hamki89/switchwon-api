package com.switchwon.switchwonapi.payment.entity;

import com.switchwon.switchwonapi.support.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class PaymentDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;

    private String expiryDate;

    private String cvv;

    private Double chargeAmount;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public void setCardInfo(String cardNumber, String expiryDate, String cvv, Double chargeAmount) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.chargeAmount = chargeAmount;
    }
}
