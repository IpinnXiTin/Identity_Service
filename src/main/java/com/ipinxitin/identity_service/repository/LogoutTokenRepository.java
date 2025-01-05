package com.ipinxitin.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipinxitin.identity_service.entity.InvalidatedToken;

@Repository
public interface LogoutTokenRepository extends JpaRepository<InvalidatedToken, String> {
    
}
