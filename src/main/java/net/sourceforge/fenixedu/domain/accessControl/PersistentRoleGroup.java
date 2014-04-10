package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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
        PersistentRoleGroup instance = role.getRoleGroup();
        return instance != null ? instance : create(role);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentRoleGroup create(final Role role) {
        PersistentRoleGroup instance = role.getRoleGroup();
        return instance != null ? instance : new PersistentRoleGroup(role);
    }
}
