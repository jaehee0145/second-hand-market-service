package com.itembay.repository;

import com.itembay.domain.Item;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Item i where i.id = :id")
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")}) // 3초 대기 후 실패
    Optional<Item> findByIdWithLock(@Param("id") Long id);
}
