package net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice;

import net.sourceforge.fenixedu.domain.accessControl.PermissionType;

public class GenericAdministrativeOfficePermission extends GenericAdministrativeOfficePermission_Base {

    private GenericAdministrativeOfficePermission() {
	super();
    }

    GenericAdministrativeOfficePermission(final AdministrativeOfficePermissionGroup group, final PermissionType type) {
	this();
	init(group, type);
    }
}
