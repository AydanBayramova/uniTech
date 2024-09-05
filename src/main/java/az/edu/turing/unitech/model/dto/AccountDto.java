package az.edu.turing.unitech.model.dto;

import az.edu.turing.unitech.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private Long accountId;

    private String accountNumber;

    private BigDecimal balance;

    private String password;

    private String cardNumber;

    private UserEntity user;
}
