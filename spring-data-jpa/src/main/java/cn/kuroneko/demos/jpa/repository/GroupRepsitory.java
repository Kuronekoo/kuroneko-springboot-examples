package cn.kuroneko.demos.jpa.repository;

import cn.kuroneko.demos.jpa.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupRepsitory extends JpaRepository<Group,Long>, JpaSpecificationExecutor<Group> {
}
