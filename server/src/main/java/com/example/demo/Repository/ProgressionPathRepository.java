package com.example.demo.Repository;

import com.example.demo.Entity.ProgressionPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressionPathRepository extends JpaRepository<ProgressionPath, Long> {
    List<ProgressionPath> findByUserId(Long userId);
    Optional<ProgressionPath> findById(Long id);
    List<ProgressionPath> findByMemoId(Long memoId);
}
