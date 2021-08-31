package com.sunrider.parfume.repository;

import com.sunrider.parfume.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByPasswordResetCode(String code);

    User findByActivationCode(String code);
}
