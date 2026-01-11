package org.backend.repository;

import org.backend.entity.RefreshToken;
import org.backend.entity.TokenVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    RefreshToken findByRefreshToken(String refreshToken);

    RefreshToken findByUserEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE TokenVerification c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.tokenVerification = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

}