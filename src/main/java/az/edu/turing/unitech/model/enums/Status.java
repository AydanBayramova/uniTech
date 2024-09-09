package az.edu.turing.unitech.model.enums;

import jakarta.persistence.Enumerated;
import org.springframework.stereotype.Component;

@Component
public enum Status {
    ACTIVE, DEACTIVATE, DELETED
}
