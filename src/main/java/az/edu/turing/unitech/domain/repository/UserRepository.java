package az.edu.turing.unitech.domain.repository;

import az.edu.turing.unitech.domain.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLastNameAndFirstName(String firstName, String lastName);

}
