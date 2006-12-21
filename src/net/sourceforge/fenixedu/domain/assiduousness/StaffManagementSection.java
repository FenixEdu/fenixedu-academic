package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class StaffManagementSection extends StaffManagementSection_Base {

    public StaffManagementSection() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public static boolean isMember(Person person) {
        return RootDomainObject.getInstance().getStaffManagementSections().isEmpty() ? false
                : RootDomainObject.getInstance().getStaffManagementSectionsIterator().next()
                        .getSectionManagers().isMember(person);
    }

}
