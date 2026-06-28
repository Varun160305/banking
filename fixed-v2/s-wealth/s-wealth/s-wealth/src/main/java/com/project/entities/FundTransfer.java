package com.project.entities;

import jakarta.persistence.*;
import com.project.enums.Currency;
import com.project.enums.TransferType;
import com.project.enums.TransferStatus;

import java.time.LocalDate;

import lombok.*;

@Entity
@Table(name = "fund_transfer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transferId;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private BankAccount fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private BankAccount toAccount;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @Column(length = 500)
    private String remarks;

    private LocalDate transferDate;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;
}