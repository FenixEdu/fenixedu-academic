package net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AdministrativeOfficePermission extends AdministrativeOfficePermission_Base {

    private AdministrativeOfficePermission() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    AdministrativeOfficePermission(final AdministrativeOfficePermissionGroup group, final PermissionType type) {
	this();
	setAdministrativeOfficePermissionGroup(group);
	setPermissionType(type);
	setPermissionMembersGroup(new FixedSetGroup());
    }

    @Service
    @Checked("PermissionPredicates.MANAGE_PERMISSION_MEMBERS_GROUP")
    public void setNewGroup(final List<Person> persons) {
	setPermissionMembersGroup(new FixedSetGroup(persons));
    }

    public boolean hasType(final PermissionType type) {
	return getPermissionType().equals(type);
    }

    public boolean isMember(final Person person) {
	return getPermissionMembersGroup().isMember(person);
    }
    
    public boolean isAppliable(final DomainObject obj) {
	return true;
    }
    
    final public boolean isAppliable(final Person person, final DomainObject obj) {
	return isMember(person) && isAppliable(obj);
    }


}
