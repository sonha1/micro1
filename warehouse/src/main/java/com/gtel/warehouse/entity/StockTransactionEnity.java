package com.gtel.warehouse.entity;

import com.gtel.warehouse.enums.StockTransactionAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stock_transactions")
@Getter
@Setter
public class StockTransactionEnity extends BaseEntity{
    @Column(name = "stock_id")
    private Long stockId;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private StockTransactionAction action;
    @Column(name = "note")
    private String note;
    @Column(name = "user_id")
    private Long userId;
}
