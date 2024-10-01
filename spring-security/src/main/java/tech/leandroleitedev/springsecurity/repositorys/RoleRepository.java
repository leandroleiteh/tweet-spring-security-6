package tech.leandroleitedev.springsecurity.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.leandroleitedev.springsecurity.entitys.EnumRoleName;
import tech.leandroleitedev.springsecurity.entitys.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(EnumRoleName enumRoleName);
}
