package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

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
	return hasExecutionCourse() ? this.getExecutionCourse().getProfessorshipsCount() : 0;
    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> elements = super.buildSet();
	if (hasExecutionCourse()) {
	    final Collection<Professorship> professorships = getExecutionCourse().getProfessorships();
	    final Collection<Person> persons = CollectionUtils.collect(professorships, new ProfessorshipPersonTransformer());
	    elements.addAll(persons);
	}

	return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasTeacher() && hasExecutionCourse()) {
	    for (final Professorship professorship : getExecutionCourse().getProfessorshipsSet()) {
		if (professorship.getTeacher() == person.getTeacher()) {
		    return true;
		}
	    }
	}
	return false;
    }
    
    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] {
                new IdOperator(getObject())
        };
    }

    public static class Builder implements GroupBuilder {

        public Group build(Object[] arguments) {
            try {
                return new ExecutionCourseTeachersGroup((ExecutionCourse) arguments[0]);
            }
            catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.executionCourse.notExecutionCourse", arguments[0].toString());
            }
        }

        public int getMinArguments() {
            return 0;
        }

        public int getMaxArguments() {
            return 1;
        }
        
    }
}
