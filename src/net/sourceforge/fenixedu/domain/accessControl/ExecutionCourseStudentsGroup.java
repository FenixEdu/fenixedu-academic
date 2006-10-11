package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.Transformer;

public class ExecutionCourseStudentsGroup extends ExecutionCourseGroup {
    private static final long serialVersionUID = 1L;

    private class AttendPersonTransformer implements Transformer {

	public Object transform(Object object) {
	    Attends attend = (Attends) object;
	    return attend.getAluno().getPerson();
	}
    }

    public ExecutionCourseStudentsGroup(ExecutionCourse executionCourse) {
	super(executionCourse);
    }

    @Override
    public int getElementsCount() {
	return this.getExecutionCourse().getAttendsCount();
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();
	Collection<Attends> attendss = this.getExecutionCourse().getAttends();
	Collection<Person> persons = CollectionUtils.collect(attendss, new AttendPersonTransformer());
	elements.addAll(persons);

	return super.freezeSet(elements);
    }
    
    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasStudent()) {
            for (final Attends attends : getExecutionCourse().getAttendsSet()) {
        	if (attends.getAluno().getStudent() == person.getStudent()) {
        	    return true;
        	}
            }
        }
	return false;
    }

}
