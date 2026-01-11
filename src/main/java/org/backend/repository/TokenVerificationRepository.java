package org.backend.repository;

import org.backend.entity.TokenVerification;

import org.backend.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Repository
public interface TokenVerificationRepository extends JpaRepository<TokenVerification, Integer> {

    TokenVerification findByTokenVerification(String token);

    @Transactional
    @Modifying
    @Query("UPDATE TokenVerification c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.tokenVerification = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

}