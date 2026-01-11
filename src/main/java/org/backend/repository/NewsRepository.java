package org.backend.repository;

import org.backend.entity.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<Books, Integer> {
    Optional<Books> findById(int id);
    Page<Books> findByCategory(String category, Pageable pageable);
}