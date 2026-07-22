package com.salman.week1.repository;

import com.salman.week1.model.entity.Member;
import com.salman.week1.model.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    @Query("""
            SELECT m
            FROM Member m
            WHERE (:status IS NULL OR m.status = :status)
            """)
    Page<Member> findAllByStatus(@Param("status") MemberStatus status, Pageable pageable);
}
