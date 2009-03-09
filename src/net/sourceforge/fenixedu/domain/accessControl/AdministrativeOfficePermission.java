package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AdministrativeOfficePermission extends AdministrativeOfficePermission_Base {
    
    public  AdministrativeOfficePermission() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public  AdministrativeOfficePermission(PermissionType type, AdministrativeOfficePermissionGroup group) {
	this();
	setPermissionType(type);
	setAdministrativeOfficePermissionGroup(group);
	setPermissionMembersGroup(new FixedSetGroup());
    }
    
    @Service
    @Checked("PermissionPredicates.managePermissionMembersGroup")
    public void setNewGroup(Group newGroup) {
	this.setPermissionMembersGroup(newGroup);
    }
    
}
