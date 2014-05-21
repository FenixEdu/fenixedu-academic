package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentRoleGroup extends PersistentRoleGroup_Base {
    protected PersistentRoleGroup(Role role) {
        super();
        this.setRole(role);
    }

    @Override
    public Group toGroup() {
        return RoleGroup.get(getRole());
    }

    public static PersistentRoleGroup getInstance(final RoleType role) {
        return getInstance(Role.getRoleByRoleType(role));
    }

    public static PersistentRoleGroup getInstance(final Role role) {
        return singleton(() -> Optional.ofNullable(role.getRoleGroup()), () -> new PersistentRoleGroup(role));
    }
}
