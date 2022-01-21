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

    @Query(value = "select count(*) from app_user where email =:email" , nativeQuery = true)
    BigInteger getCountEmail(@Param("email") String email);

    @Query(value = "select * from app_user where email =:email" , nativeQuery = true)
     User getByEmail(@Param("email") String email);
}
