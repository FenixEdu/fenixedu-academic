package org.fenixedu.academic.domain.groups;

import java.util.Optional;

import org.fenixedu.bennu.core.domain.Bennu;

public class PersistentPermissionGroup extends PersistentPermissionGroup_Base {
    
    public PersistentPermissionGroup(String code) {
        super();
        this.setCode(code);
        this.setPermissionGroupRoot(Bennu.getInstance());
    }

    public static Optional<PersistentPermissionGroup> getInstance(String code) {
        return Bennu.getInstance().getPermissionGroupSet().stream().filter(group -> group.getCode().equals(code))
                .findAny();
    }

    @Override
    public PermissionGroup toGroup() {
        return new PermissionGroup(this.getCode());
    }
    
}
