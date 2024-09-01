package az.edu.turing.unitech.model.dto;

import az.edu.turing.unitech.domain.entity.AccountEntity;

import java.time.LocalDateTime;
import java.util.List;

public class UserDto {

    private String firstName;

    private String lastName;

    private String pin;

    private LocalDateTime updateDate;

    private LocalDateTime createDate;

    private List<AccountEntity> accountEntities;

}
