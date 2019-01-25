package com.cs.orderbookmanagement.repository;

import com.cs.orderbookmanagement.models.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository extends JpaRepository<Execution, Integer> {
}

