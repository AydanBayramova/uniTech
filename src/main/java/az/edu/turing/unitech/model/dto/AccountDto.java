package az.edu.turing.unitech.model.dto;

import az.edu.turing.unitech.model.enums.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @NotNull(message = "accountId cannot be null")
    private Long accountId;

    private AccountStatus status;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, unique = true, length = 16)
    private String cardNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotBlank(message = "AccounStatus is required")
    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;

}
