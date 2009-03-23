package net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;

public class AdministrativeOfficePermissionGroup extends AdministrativeOfficePermissionGroup_Base {

    private AdministrativeOfficePermissionGroup() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public AdministrativeOfficePermissionGroup(final AdministrativeOffice administrativeOffice, final Campus campus) {
	this();
	setAdministrativeOffice(administrativeOffice);
	setCampus(campus);
    }

    public void createPermissionForType(final PermissionType permissionType) {
	if (hasPermission(permissionType)) {
	    throw new DomainException("error.AdministrativeOfficePermissionGroup.already.has.permission.with.type",
		    permissionType.getLocalizedName());
	}
	new AdministrativeOfficePermission(this, permissionType);
    }

    public boolean hasPermission(final PermissionType permissionType) {
	return getPermission(permissionType) != null;
    }

    public AdministrativeOfficePermission getPermission(final PermissionType permissionType) {
	for (final AdministrativeOfficePermission permission : getAdministrativeOfficePermissions()) {
	    if (permission.hasType(permissionType)) {
		return permission;
	    }
	}
	return null;
    }

    public boolean hasCampus(final Campus campus) {
	return hasCampus() && getCampus().equals(campus);
    }

}
