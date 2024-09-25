package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.exception.AccountNotFoundException;
import az.edu.turing.unitech.exception.CommonBadRequestException;
import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.dto.AccountToAccountRequest;
import az.edu.turing.unitech.model.enums.Status;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.service.AccountService;
import az.edu.turing.unitech.service.Notification;
import az.edu.turing.unitech.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public void accountToAccountTransfer(AccountToAccountRequest request) {
        AccountEntity sender = accountRepository.findByAccountNumberAndStatus(request.fromAccountNumber(), Status.ACTIVE)
                .orElseThrow(()-> new AccountNotFoundException("no such active account"));
        checkAmountAndBalance(sender.getBalance(), request.amount());

        AccountEntity receiver = accountRepository.findByAccountNumberAndStatus(request.toAccountNumber(), Status.ACTIVE)
                .orElseThrow(()-> new AccountNotFoundException("no such active account"));
        validateReceiverAccount(accountMapper.accountEntityToDto(sender), accountMapper.accountEntityToDto(receiver));

        sender.setBalance(sender.getBalance().subtract(request.amount()));
       accountRepository.save(sender);

        receiver.setBalance(receiver.getBalance().add(request.amount()));
        accountRepository.save(receiver);
    }


    private void checkAmountAndBalance(BigDecimal balance, BigDecimal amountToTransfer) {
        if (amountToTransfer == null || amountToTransfer.compareTo(BigDecimal.ONE) < 0) {
            throw new CommonBadRequestException("Invalid amount! The amount must be greater than or equal to 1.");
        }
        if (balance == null || balance.compareTo(amountToTransfer) < 0) {
            throw new CommonBadRequestException("Insufficient balance!");
        }
    }

    private void validateReceiverAccount(AccountDto senderAccount, AccountDto receiverAccount) {
        if (receiverAccount.equals(senderAccount))
            throw new CommonBadRequestException("Transfer into the same account not permitted!");
    }
}
