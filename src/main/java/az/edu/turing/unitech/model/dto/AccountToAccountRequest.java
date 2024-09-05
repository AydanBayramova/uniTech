package az.edu.turing.unitech.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountToAccountRequest(
        @NotNull(message = "From account number cannot be null")
        String fromAccountNumber,

        @NotNull(message = "To account number cannot be null")
        String toAccountNumber,

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount
) {
}