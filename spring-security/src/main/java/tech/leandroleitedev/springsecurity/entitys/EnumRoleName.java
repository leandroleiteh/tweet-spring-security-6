package tech.leandroleitedev.springsecurity.entitys;

import lombok.Getter;

@Getter
public enum EnumRoleName {
    ADMIN(1L),
    BASIC(2L);

    final long roleId;

    EnumRoleName(long roleId) {
        this.roleId = roleId;
    }
}
