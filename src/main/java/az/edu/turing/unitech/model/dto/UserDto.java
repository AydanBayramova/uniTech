package az.edu.turing.unitech.model.dto;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstName;

    private String lastName;

    private String password;

    private String pin;

    private String email;

    private LocalDateTime updateDate;

    private LocalDateTime createDate;

    private List<AccountEntity> accountEntities;

}
