package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.service.Notification;
import az.edu.turing.unitech.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final Notification notification;

    @Override
    public void transferToSameBank(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount) {

    }

}
