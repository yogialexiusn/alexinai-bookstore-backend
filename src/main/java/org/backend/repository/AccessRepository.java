package org.backend.repository;

import org.backend.entity.Access;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, Integer> {
    List<Access> findByUsername(String username);
    Access findByUsernameAndMenuAccess(String username, String menuAccess);
}
