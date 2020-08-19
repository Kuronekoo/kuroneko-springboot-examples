package cn.kuroneko.demos.commons.jpa.repository;

import cn.kuroneko.demos.commons.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

}
