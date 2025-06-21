package com.example.demo.Repository;

import com.example.demo.Entity.MemoStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoStageRepository extends JpaRepository<MemoStage, Long> {
    List<MemoStage> findByMemoIdOrderByStageNumber(Long memoId);
}