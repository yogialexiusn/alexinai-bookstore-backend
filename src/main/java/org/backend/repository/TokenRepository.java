package org.backend.repository;

import org.backend.entity.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findByToken(String tokenString);

    @Transactional
    @Modifying
    @Query("UPDATE Token c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

}