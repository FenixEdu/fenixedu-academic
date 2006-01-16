package net.sourceforge.fenixedu.domain.department;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;

// TODO: check this with Luís Egídio
public class CompetenceCourseMembersGroup extends GroupUnion {
    
    private DomainReference<Department> department;
    
    public CompetenceCourseMembersGroup(Department department) {
        super();
        
        this.department = new DomainReference<Department>(department);
    }
    
    public Department getDepartment() {
        return this.department.getObject();
    }
}
