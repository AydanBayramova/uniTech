package az.edu.turing.unitech.model.dto;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull(message="ID can not be null")
    private Long id;

    @Column(name = "firstname", nullable = false, length = 15)
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 15)
    private String lastName;

    @Column(name = "pin", nullable = false, unique = true, length = 7)
    @Size(min = 7, max = 7, message = ("Pin must be exactly 7 symbol!"))
    private String pin;

    private LocalDateTime updateDate;

    private LocalDateTime createDate;

}
