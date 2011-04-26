package net.sourceforge.fenixedu.domain.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class DepartmentForum extends DepartmentForum_Base {

    public DepartmentForum() {
	super();
    }

    @Override
    public Group getReadersGroup() {
	return getDepartmentForumGroup();
    }

    @Override
    public Group getWritersGroup() {
	return getDepartmentForumGroup();
    }

    @Override
    public Group getAdminGroup() {
	return new FixedSetGroup(getDepartmentManagers());
    }

    public Department getDepartment() {
	for (Department department : RootDomainObject.getInstance().getDepartments()) {
	    if (this.equals(department.getDepartmentForum())) {
		return department;
	    }
	}
	return null;
    }

    public Group getDepartmentForumGroup() {
	Collection<IGroup> groups = new ArrayList<IGroup>();
	Department department = getDepartment();
	groups.add(new DepartmentEmployeesGroup(department));
	groups.add(new FixedSetGroup(getPersonsFromTeachers(department)));
	return new GroupUnion(groups);
    }

    private List<Person> getDepartmentManagers() {
	return getDepartment().getDepartmentUnit().getSite().getManagers();
    }

    private List<Person> getPersonsFromTeachers(Department department) {
	List<Person> persons = new ArrayList<Person>();
	for (Teacher teacher : department.getAllCurrentTeachers()) {
	    persons.add(teacher.getPerson());
	}
	return persons;
    }
}
