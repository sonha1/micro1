package com.gtel.shipping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter
@Setter
public class WalletEntity extends BaseEntity {
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "status")
    private Integer status;
}
