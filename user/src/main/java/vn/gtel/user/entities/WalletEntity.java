package vn.gtel.user.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter
@Setter
public class WalletEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private BigDecimal balance;
}
