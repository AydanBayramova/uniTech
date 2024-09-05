package az.edu.turing.unitech.domain.entity;

import az.edu.turing.unitech.model.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET status= 'DEACTIVATE' WHERE id=?")
@FilterDef(name = "statusFilter", parameters = @ParamDef(name = "status", type = AccountStatus.class))
@Filter(name = "statusFilter", condition = "status = :status")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false, length = 20)
    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
