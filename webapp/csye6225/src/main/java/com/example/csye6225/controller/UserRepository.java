package com.example.csye6225.controller;
import com.example.csye6225.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
