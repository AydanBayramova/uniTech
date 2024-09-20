package az.edu.turing.unitech.model.dto;

import lombok.Data;

@Data

public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
