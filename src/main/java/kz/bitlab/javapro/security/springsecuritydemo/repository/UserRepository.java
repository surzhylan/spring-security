package kz.bitlab.javapro.security.springsecuritydemo.repository;

import kz.bitlab.javapro.security.springsecuritydemo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findAllByEmail(String email);
}
