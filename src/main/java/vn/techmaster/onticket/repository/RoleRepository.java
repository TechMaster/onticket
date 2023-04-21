package vn.techmaster.onticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.techmaster.onticket.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByName(String name);
}

