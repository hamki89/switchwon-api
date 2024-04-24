package com.switchwon.switchwonapi.user.repository;

import com.switchwon.switchwonapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
