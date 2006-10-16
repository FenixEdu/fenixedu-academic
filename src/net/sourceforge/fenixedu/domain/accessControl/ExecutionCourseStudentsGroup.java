package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
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
	return super.freezeSet(new HashSet<Person>(CollectionUtils.collect(getExecutionCourse()
		.getAttends(), new AttendPersonTransformer())));
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasStudent() && hasExecutionCourse()) {
	    for (final Attends attends : getExecutionCourse().getAttendsSet()) {
		if (attends.getAluno().getStudent() == person.getStudent()) {
		    return true;
		}
	    }
	}
	return false;
    }

}
