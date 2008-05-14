/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 13, 2006,5:03:44 PM
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 13, 2006,5:03:44 PM
 * 
 */
public class ExecutionCourseResponsiblesGroup extends Group {
    
    private static final long serialVersionUID = -1670838873686375271L;

    @Override
    public Set<Person> getElements() {
	final Set<Person> elements = super.buildSet();
	final Collection<ExecutionYear> executionYears = RootDomainObject.getInstance().getExecutionYears();
	for (final ExecutionYear executionYear : executionYears) {
	    if (executionYear.isCurrent()) {
		for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
		    for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCourses()) {
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

    @Override
    protected Argument[] getExpressionArguments() {
        return null;
    }

}
