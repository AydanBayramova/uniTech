package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.exception.CommonBadRequestException;
import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.dto.AccountToAccountRequest;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.service.AccountService;
import az.edu.turing.unitech.service.Notification;
import az.edu.turing.unitech.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final Notification notification;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    public void accountToAccountTransfer(AccountToAccountRequest request) {
        AccountDto sender = accountService.getActiveAccountByAccountNumber(request.fromAccountNumber()).get();
        checkAmountAndBalance(sender.getBalance(), request.amount());

        AccountDto receiver = accountService.getActiveAccountByAccountNumber(request.toAccountNumber()).get();
        validateReceiverAccount(sender, receiver);

        sender.setBalance(sender.getBalance().subtract(request.amount()));
        accountRepository.save(accountMapper.accountDtoToAccountEntity(sender));

        receiver.setBalance(receiver.getBalance().add(request.amount()));
        accountRepository.save(accountMapper.accountDtoToAccountEntity(receiver));
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
