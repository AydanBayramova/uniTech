package az.edu.turing.unitech.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String password;
    private String pin;
}
