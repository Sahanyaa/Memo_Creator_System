package com.example.demo.Repository;

import com.example.demo.Entity.ForwardMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForwardMemoRepository extends JpaRepository<ForwardMemo, Long> {
}
