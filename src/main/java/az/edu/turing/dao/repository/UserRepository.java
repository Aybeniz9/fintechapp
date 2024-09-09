package az.edu.turing.dao.repository;

import az.edu.turing.dao.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
   void deleteByFinCode(String finCode);
   Optional<UserEntity> findByFinCode(String finCode);

    boolean existsByFinCode(@NotBlank String finCode);
}
