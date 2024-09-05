package az.edu.turing.unitech.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface TransferService {

    void transferToSameBank(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount);

}
