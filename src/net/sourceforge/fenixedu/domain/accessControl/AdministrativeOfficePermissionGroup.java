package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.Campus;

public class AdministrativeOfficePermissionGroup extends AdministrativeOfficePermissionGroup_Base {
    
    public  AdministrativeOfficePermissionGroup() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public  AdministrativeOfficePermissionGroup(Campus campus) {
	this();
	setCampus(campus);
    }
    
    public void createPermissionForType(PermissionType permissionType) {
	new AdministrativeOfficePermission(permissionType, this);
    }
    
    public AdministrativeOfficePermission getPermissionByType(PermissionType type) {
	for(AdministrativeOfficePermission permission : getAdministrativeOfficePermissions()) {
	    if(permission.getPermissionType().equals(type)) {
		return permission;
	    }
	}
	
	return null;
    }
    
}
