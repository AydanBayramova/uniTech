package az.edu.turing.unitech.domain.repository;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.model.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountNumberAndStatus(String accountNumber, AccountStatus status);
}
