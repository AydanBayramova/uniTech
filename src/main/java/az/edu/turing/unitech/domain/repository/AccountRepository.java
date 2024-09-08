package az.edu.turing.unitech.domain.repository;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByAccountNumberAndStatus(String accountNumber, Status status);

    void deleteByAccountNumber(String accountNumber);

    Page<AccountEntity> getAllByStatus(Status status, Pageable pageable);

    Optional<AccountEntity> findByUserAndAccountNumber(UserEntity user, String accountNumber);

    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
