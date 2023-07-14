package com.costler.userproject.repo;
import com.costler.userproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
    User findByEmail(String email);
    void deleteByEmail(String email);

}
