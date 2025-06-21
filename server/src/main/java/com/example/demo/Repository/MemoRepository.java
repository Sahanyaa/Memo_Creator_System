package com.example.demo.Repository;

import com.example.demo.Entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByMemoCreatorId(Integer memoCreatorId);
}
