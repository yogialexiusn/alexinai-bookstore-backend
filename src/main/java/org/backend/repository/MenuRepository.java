package org.backend.repository;

import org.backend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findAllByActive(Boolean status);
}
