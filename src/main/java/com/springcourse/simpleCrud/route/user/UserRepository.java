package com.springcourse.simpleCrud.route.user;

import com.springcourse.simpleCrud.model.schema.UserProfile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, Integer> {

    @Query("SELECT u FROM UserProfile u WHERE u.email = :email")
    Optional<UserProfile> findByEmail(String email);
}
