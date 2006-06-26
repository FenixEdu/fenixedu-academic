package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentGroup;

public class StudentGroupStudentsGroup extends DomainBackedGroup<StudentGroup> {

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
}
