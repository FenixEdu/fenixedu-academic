package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.InternalPersonBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.Researcher;
import pt.ist.fenixWebFramework.services.Service;

public class CreateNewInternalPerson {

    @Service
    public static Person run(final InternalPersonBean bean) {
	final Person person = new Person(bean);
	final Set<RoleType> roleTypes = bean.getRelationTypes();
	attributeRoles(person, roleTypes);
	return person;
    }

    @Service
    public static void attributeRoles(final Person person, final Set<RoleType> roleTypes) {
	if (roleTypes.isEmpty()) {
	    throw new DomainException("error.create.internal.person.relation.type.none");
	}

	if (roleTypes.contains(RoleType.TEACHER)) {
	    createTeacher(person);
	}
	if (roleTypes.contains(RoleType.RESEARCHER) || roleTypes.contains(RoleType.TEACHER)) {
	    createResearcher(person);
	}
	if (roleTypes.contains(RoleType.EMPLOYEE) || roleTypes.contains(RoleType.RESEARCHER)
		|| roleTypes.contains(RoleType.TEACHER)) {
	    createEmployee(person);
	}
	if (roleTypes.contains(RoleType.GRANT_OWNER)) {
	    createGrantOwner(person);
	}
    }

    private static void createTeacher(final Person person) {
	if (!person.hasTeacher()) {
	    final Integer number = person.hasEmployee() ? person.getEmployee().getEmployeeNumber() : Employee
		    .getNextEmployeeNumber();
	    new Teacher(person);
	    // TODO : check and complete this.
	}
	person.addPersonRoleByRoleType(RoleType.TEACHER);
    }

    private static void createResearcher(final Person person) {
	if (!person.hasResearcher()) {
	    new Researcher(person);
	    // Is there a number to set in this case?
	    // TODO : check and complete this.
	}
	person.addPersonRoleByRoleType(RoleType.RESEARCHER);
    }

    private static void createEmployee(final Person person) {
	if (!person.hasEmployee()) {
	    final Integer number = Employee.getNextEmployeeNumber();
	    new Employee(person, number, Boolean.TRUE);
	    // TODO : check and complete this.
	}
	person.addPersonRoleByRoleType(RoleType.EMPLOYEE);
    }

    private static void createGrantOwner(final Person person) {
	if (!person.hasGrantOwner()) {
	    final GrantOwner grantOwner = new GrantOwner();
	    grantOwner.setPerson(person);
	    final Integer number = new Integer(GrantOwner.readMaxGrantOwnerNumber().intValue() + 1);
	    grantOwner.setNumber(number);
	    // TODO : check and complete this.
	}
	person.addPersonRoleByRoleType(RoleType.GRANT_OWNER);
    }

}
