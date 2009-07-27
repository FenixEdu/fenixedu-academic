package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TutorSearchBean implements Serializable {
    public static class DepartmentTeachersProvider implements DataProvider {
	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object current) {
	    TutorSearchBean bean = (TutorSearchBean) source;
	    Set<Teacher> teachers = new HashSet<Teacher>();
	    if (bean.getDepartment() != null) {
		for (Employee employee : bean.getDepartment().getAllCurrentActiveWorkingEmployees()) {
		    if (employee.getPerson().getTeacher() != null) {
			if (!bean.getOnlyTutors() || employee.getPerson().getTeacher().hasAnyTutorships()) {
			    teachers.add(employee.getPerson().getTeacher());
			}
		    }
		}
	    } else {
		for (Teacher teacher : RootDomainObject.getInstance().getTeachersSet()) {
		    if (!bean.getOnlyTutors() || teacher.hasAnyTutorships()) {
			teachers.add(teacher);
		    }
		}
	    }
	    return teachers;
	}
    }

    private static final long serialVersionUID = 161580136110904806L;

    private DomainReference<Department> department;

    private DomainReference<Teacher> teacher;

    private Boolean onlyTutors = Boolean.TRUE;

    public Department getDepartment() {
	return department == null ? null : department.getObject();
    }

    public void setDepartment(Department department) {
	this.department = department == null ? null : new DomainReference<Department>(department);
    }

    public Teacher getTeacher() {
	return teacher == null ? null : teacher.getObject();
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = teacher == null ? null : new DomainReference<Teacher>(teacher);
    }

    public Boolean getOnlyTutors() {
	return onlyTutors;
    }

    public void setOnlyTutors(Boolean onlyTutors) {
	this.onlyTutors = onlyTutors;
    }

    public void setOnlyTutors(String onlyTutors) {
	this.onlyTutors = Boolean.valueOf(onlyTutors);
    }
}