package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.DomainBackedGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class InternalOrExternalTeacherGroup extends DomainBackedGroup<Person> {

    public InternalOrExternalTeacherGroup() {
	super(null);
    }

    @Override
    public boolean equals(Object other) {
	boolean result = other != null;

	if (result) {
	    result = other.getClass().equals(this);
	}

	return result;
    }

    @Override
    public int hashCode() {
	return getClass().hashCode();
    }

    @Override
    public boolean isMember(Person person) {

	return person != null && (person.hasRole(RoleType.TEACHER) || person.hasAnyProfessorships());
    }

    @Override
    public Set<Person> getElements() {
	HashSet<Person> set = new HashSet<Person>();

	for (Party party : RootDomainObject.getInstance().getPartys()) {
	    if (party instanceof Person && ((Person) party).hasAnyProfessorships()) {
		set.add((Person) party);
	    }
	}

	for (Teacher teacher : RootDomainObject.getInstance().getTeachers()) {
	    set.add(teacher.getPerson());
	}

	return set;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] {};
    }

    public static class Builder implements GroupBuilder {

	@Override
	public Group build(Object[] arguments) {
	    return new InternalOrExternalTeacherGroup();
	}

	@Override
	public int getMaxArguments() {
	    return 1;
	}

	@Override
	public int getMinArguments() {
	    return 0;
	}

    }

}
