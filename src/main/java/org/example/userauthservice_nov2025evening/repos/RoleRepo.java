package org.example.userauthservice_nov2025evening.repos;

import org.example.userauthservice_nov2025evening.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    Optional<Role> findByValue(String value);
}
