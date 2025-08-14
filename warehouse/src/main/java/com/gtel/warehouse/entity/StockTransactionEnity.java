package com.gtel.warehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private String action;
    @Column(name = "note")
    private String note;
}
