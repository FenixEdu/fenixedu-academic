package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CompetenceCourse extends CompetenceCourse_Base {
    
    public CompetenceCourse(String code, String name, Collection<IDepartment> departments) {
    	fillfields(code, name, departments);
    }
    
    private void fillfields(String code, String name, Collection<IDepartment> departments) {
    	if(code == null || code.length() == 0) {
			throw new DomainException("Invalid code argument");
		}
		if(name == null || name.length() == 0) {
			throw new DomainException("Invalid name argument");
		}
		
        setCode(code);
        setName(name);
        if(departments != null) {
        	addDepartments(departments);
        }
	}
    
    public void edit(String code, String name, Collection<IDepartment> departments) {
    	fillfields(code, name, departments);
    }

	public void delete() {
		getDepartments().clear();
    	getAssociatedCurricularCourses().clear();
    	super.deleteDomainObject();
    }
    
    public void addCurricularCourses(Collection<ICurricularCourse> curricularCourses) {
    	for (ICurricularCourse curricularCourse : curricularCourses) {
    		addAssociatedCurricularCourses(curricularCourse);
		}
    }
    
    public void addDepartments(Collection<IDepartment> departments) {
    	for (IDepartment department : departments) {
			addDepartments(department);
		}
    }
}
