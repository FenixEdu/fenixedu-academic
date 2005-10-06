package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CompetenceCourse extends CompetenceCourse_Base {
    
    public CompetenceCourse(String code, String name, Collection<IDepartment> departments) {
    	fillFields(code, name);
        if(departments != null) {
        	addDepartments(departments);
        }
    }
    
    private void fillFields(String code, String name) {
    	if(code == null || code.length() == 0) {
			throw new DomainException("Invalid code argument");
		}
		if(name == null || name.length() == 0) {
			throw new DomainException("Invalid name argument");
		}
		
        setCode(code);
        setName(name);
	}
    
    public void edit(String code, String name, Collection<IDepartment> departments) {
    	fillFields(code, name);
    	for (IDepartment department : getDepartments()) {
			if(!departments.contains(department)) {
				removeDepartments(department);
			}
		}
    	for (IDepartment department : departments) {
			if(!hasDepartments(department)) {
				addDepartments(department);
			}
		}
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
