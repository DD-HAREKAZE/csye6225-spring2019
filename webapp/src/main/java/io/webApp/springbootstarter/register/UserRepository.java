package io.webApp.springbootstarter.register;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<register,Integer> {

    register findByEmail(String email);

}
