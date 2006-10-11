package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;

import org.apache.commons.collections.Transformer;

public class ExecutionCourseTeachersGroup extends ExecutionCourseGroup {

    private static final long serialVersionUID = -4575035849468586468L;

    private class ProfessorshipPersonTransformer implements Transformer {

	public Object transform(Object arg0) {
	    Professorship professorship = (Professorship) arg0;

	    return professorship.getTeacher().getPerson();
	}
    }

    public ExecutionCourseTeachersGroup(ExecutionCourse executionCourse) {
	super(executionCourse);
    }

    @Override
    public int getElementsCount() {
	return this.getExecutionCourse().getProfessorshipsCount();
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();
	Collection<Professorship> professorships = this.getExecutionCourse().getProfessorships();
	Collection<Person> persons = CollectionUtils.collect(professorships,
		new ProfessorshipPersonTransformer());

	elements.addAll(persons);

	return super.freezeSet(elements);
    }
    
    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasTeacher()) {
            for (final Professorship professorship : getExecutionCourse().getProfessorshipsSet()) {
        	if (professorship.getTeacher() == person.getTeacher()) {
        	    return true;
        	}
            }
        }
        return false;
    }
}
