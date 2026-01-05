package org.example.userauthservice_nov2025evening.repos;

import org.example.userauthservice_nov2025evening.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session,Long> {
    Optional<Session> findByToken(String token);
}
