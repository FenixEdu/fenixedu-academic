package net.sourceforge.fenixedu.domain.department;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;

public class CompetenceCourseMembersGroup extends CompetenceCourseMembersGroup_Base {
    
    public CompetenceCourseMembersGroup() {
        super();
    }
    
    public CompetenceCourseMembersGroup(Person creator, Department department) {
        super();
        setCreator(creator);
        setDepartment(department);
    }
}
