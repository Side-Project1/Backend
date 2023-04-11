package com.project.server.repository;

import com.project.server.entity.ConfirmMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmMailRepository extends JpaRepository<ConfirmMail, UUID> {
    Optional<ConfirmMail> findByNumberAndExpirationDateAfterAndExpired(Integer number, LocalDateTime now, boolean expired);
}
