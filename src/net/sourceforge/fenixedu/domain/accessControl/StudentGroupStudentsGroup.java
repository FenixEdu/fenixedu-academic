package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentGroup;

public class StudentGroupStudentsGroup extends DomainBackedGroup<StudentGroup> {

    private static final long serialVersionUID = -7462413677592415379L;

    public StudentGroupStudentsGroup(StudentGroup object) {
	super(object);
    }

    private StudentGroup getStudentGroup() {
	return this.getObject();
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();

	for (Attends attends : getStudentGroup().getAttends()) {
	    elements.add(attends.getAluno().getPerson());
	}

	return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasStudent()) {
	    for (final Attends attends : getStudentGroup().getAttendsSet()) {
		if (attends.getAluno().getStudent().getPerson() == person) {
		    return true;
		}
	    }
	}

	return false;
    }
}
