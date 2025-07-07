package br.com.generic.service.repository;

import br.com.generic.service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<UserEntity> findByEmailWithRoles(@Param("email") String email);





}
