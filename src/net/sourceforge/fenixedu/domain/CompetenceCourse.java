package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CompetenceCourse extends CompetenceCourse_Base {
    
    public CompetenceCourse(String code, String name, IDepartment department) {
    	fillfields(code, name, department);
    }
    
    private void fillfields(String code, String name, IDepartment department) {
    	if(code == null || code.length() == 0) {
			throw new DomainException("Invalid code argument");
		}
		if(name == null || name.length() == 0) {
			throw new DomainException("Invalid code argument");
		}
		
        setCode(code);
        setName(name);
        setDepartment(department);		
	}
    
    public void edit(String code, String name, IDepartment department) {
    	fillfields(code, name, department);
    }

	public void delete() {
    	removeDepartment();
    	getAssociatedCurricularCourses().clear();
    	super.deleteDomainObject();
    }
    
    public void addCurricularCourses(List<ICurricularCourse> curricularCourses) {
    	for (ICurricularCourse curricularCourse : curricularCourses) {
			if(!hasAssociatedCurricularCourses(curricularCourse)) {
				addAssociatedCurricularCourses(curricularCourse);
			}
		}
    }
}
