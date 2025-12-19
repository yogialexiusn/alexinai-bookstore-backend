package org.backend.repository;

import org.backend.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    Optional<News> findById(int id);
    Page<News> findByCategory(String category, Pageable pageable);
}