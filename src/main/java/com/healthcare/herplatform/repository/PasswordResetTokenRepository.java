package com.healthcare.herplatform.repository;

import com.healthcare.herplatform.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    @Transactional
    void deleteByEmail(String email);

    @Transactional
    void deleteByExpiryDateBefore(LocalDateTime now);
}
