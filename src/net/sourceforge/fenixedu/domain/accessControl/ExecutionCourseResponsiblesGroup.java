/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 13, 2006,5:03:44 PM
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 13, 2006,5:03:44 PM
 * 
 */
public class ExecutionCourseResponsiblesGroup extends Group {

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();
	final Collection<ExecutionYear> executionYears = RootDomainObject.getInstance()
		.getExecutionYears();
	for (final ExecutionYear executionYear : executionYears) {
	    if (executionYear.getState().equals(PeriodState.CURRENT)) {
		for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
		    for (final ExecutionCourse executionCourse : executionPeriod
			    .getAssociatedExecutionCourses()) {
			for (final Professorship professorship : executionCourse.getProfessorships()) {
			    if (professorship.getResponsibleFor().booleanValue()) {
				final Teacher teacher = professorship.getTeacher();
				final Person person = teacher.getPerson();
				elements.add(person);
			    }
			}
		    }
		}
		break;
	    }
	}
	return elements;
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasTeacher()) {
	    for (final Professorship professorship : person.getTeacher().getProfessorships(
		    ExecutionYear.readCurrentExecutionYear())) {
		if (professorship.isResponsibleFor()) {
		    return true;
		}

	    }

	}

	return false;

    }

}
