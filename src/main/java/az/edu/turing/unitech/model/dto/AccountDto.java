package az.edu.turing.unitech.model.dto;

import az.edu.turing.unitech.domain.entity.UserEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private Long accountId;

    private String accountNumber;

    private BigDecimal balance;

    private String password;

    private String cardNumber;

    private UserEntity user;
}
