package az.edu.turing.unitech.domain.repository;

import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.model.enums.Status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Page<UserEntity> findAllByStatus(Status status, Pageable pageable);


}
