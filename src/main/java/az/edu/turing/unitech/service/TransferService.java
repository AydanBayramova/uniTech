package az.edu.turing.unitech.service;

import az.edu.turing.unitech.model.dto.AccountToAccountRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface TransferService {

    void accountToAccountTransfer(AccountToAccountRequest request);

}
