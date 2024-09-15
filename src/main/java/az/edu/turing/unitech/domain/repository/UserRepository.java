package az.edu.turing.unitech.domain.repository;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByPin(String pin);

    Optional<UserEntity> findByLastNameAndFirstName(String firstName, String lastName);

    boolean existsByPin(String pin);

}
