package az.edu.turing.unitech.domain.repository;

import az.edu.turing.unitech.domain.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRole(String role);
}
