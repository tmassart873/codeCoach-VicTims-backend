package com.victims.codecoachvictimsbackend.user.repository;

import com.victims.codecoachvictimsbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "select count(*) from users where email =:email" , nativeQuery = true)
    public BigInteger getCountEmail(@Param("email") String email);
}
