package az.edu.turing.unitech.model.dto;

import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.model.enums.Status;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private String accountNumber;

    private BigDecimal balance;

    private Status status;
}
