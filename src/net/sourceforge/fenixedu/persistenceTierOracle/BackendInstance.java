package net.sourceforge.fenixedu.persistenceTierOracle;

import net.sourceforge.fenixedu.domain.person.RoleType;

public enum BackendInstance {

    IST(RoleType.PROJECTS_MANAGER, RoleType.INSTITUCIONAL_PROJECTS_MANAGER),
    IT(RoleType.IT_PROJECTS_MANAGER, null),
    IST_ID(RoleType.ISTID_PROJECTS_MANAGER, RoleType.ISTID_INSTITUCIONAL_PROJECTS_MANAGER);

    public final RoleType roleType;
    public final RoleType institutionalRoleType;

    BackendInstance(final RoleType roleType, final RoleType institutionalRoleType) {
	this.roleType = roleType;
	this.institutionalRoleType = institutionalRoleType;
    }

}
