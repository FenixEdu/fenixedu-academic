package net.sourceforge.fenixedu.domain.department;

import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IPerson;

public class CompetenceCourseMembersGroup extends CompetenceCourseMembersGroup_Base {
    
    public CompetenceCourseMembersGroup() {
        super();
    }
    
    public CompetenceCourseMembersGroup(IPerson creator, IDepartment department) {
        super();
        setCreator(creator);
        setDepartment(department);
    }
}
