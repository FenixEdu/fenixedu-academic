package net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.space.Campus;

public class AdministrativeOfficePermissionGroup extends AdministrativeOfficePermissionGroup_Base {

    private AdministrativeOfficePermissionGroup() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public AdministrativeOfficePermissionGroup(final Campus campus) {
	this();
	setCampus(campus);
    }

    public void createPermissionForType(final PermissionType permissionType) {
	new AdministrativeOfficePermission(permissionType, this);
    }

    public AdministrativeOfficePermission getPermissionByType(final PermissionType type) {
	for (final AdministrativeOfficePermission permission : getAdministrativeOfficePermissions()) {
	    if (permission.getPermissionType().equals(type)) {
		return permission;
	    }
	}

	return null;
    }

}
