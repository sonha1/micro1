package vn.gtel.user.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Table(name = "CLIENT")
@Entity
public class ClientEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "SECRET")
    private String secret;

    private int status;
}
