package com.reservas.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reservas.app.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByDate(LocalDate date);
}
