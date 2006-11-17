package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

public class DegreeTeachersGroup extends DegreeGroup {

    /**
         * 
         */
    private static final long serialVersionUID = 8466471514890333054L;

    public DegreeTeachersGroup(Degree degree) {
	super(degree);
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();

	for (DegreeCurricularPlan degreeCurricularPlan : getDegree().getActiveDegreeCurricularPlans()) {
	    for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
		for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
		    for (Professorship professorship : executionCourse.getProfessorships()) {
			elements.add(professorship.getTeacher().getPerson());
		    }
		}
	    }
	}

	return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasTeacher()) {
	    for (final Professorship professorship : person.getTeacher().getProfessorshipsSet()) {
		for (final CurricularCourse curricularCourse : professorship.getExecutionCourse()
			.getAssociatedCurricularCoursesSet()) {
		    if (curricularCourse.getDegree() == getDegree()) {
			return true;
		    }
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
                return new DegreeTeachersGroup((Degree) arguments[0]);
            }
            catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.degreeGroup.notDegree", arguments[0].toString());
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
