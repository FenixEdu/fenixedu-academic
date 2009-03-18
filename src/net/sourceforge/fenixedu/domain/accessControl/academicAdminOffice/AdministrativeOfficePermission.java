package net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AdministrativeOfficePermission extends AdministrativeOfficePermission_Base {

    private AdministrativeOfficePermission() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    AdministrativeOfficePermission(final PermissionType type, final AdministrativeOfficePermissionGroup group) {
	this();
	setPermissionType(type);
	setAdministrativeOfficePermissionGroup(group);
	setPermissionMembersGroup(new FixedSetGroup());
    }

    @Service
    @Checked("PermissionPredicates.managePermissionMembersGroup")
    public void setNewGroup(final Group newGroup) {
	this.setPermissionMembersGroup(newGroup);
    }

}
