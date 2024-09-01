package az.edu.turing.unitech.model.dto;

import az.edu.turing.unitech.domain.entity.UserEntity;
import java.math.BigDecimal;

public class AccountDto {

    private Long accountId;

    private String accountNumber;

    private BigDecimal balance;

    private String password;

    private String cardNumber;

    private UserEntity user;
}
