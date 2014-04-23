package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * This group represents the group of students associated to at least one degree
 * where a specific execution course is present
 * 
 * @author cmej
 */
public class DegreesOfExecutionCourseGroup extends ExecutionCourseGroup {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    public DegreesOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super(executionCourse);
    }

    @Override
    public Set<Person> getElements() {
        HashSet<Person> elements = new HashSet<Person>();
        if (hasExecutionCourse()) {
            List<Degree> degreeList = new ArrayList<Degree>();
            for (CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
                degreeList.add(curricularCourse.getDegree());
            }
            for (Degree degree : degreeList) {
                for (Registration registration : degree.getActiveRegistrations()) {
                    elements.add(registration.getPerson());
                }
            }
            for (ExecutionDegree executionDegree : getExecutionCourse().getExecutionDegrees()) {
                for (Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    elements.add(coordinator.getPerson());
                }
            }
            for (Attends attends : getExecutionCourse().getAttendsSet()) {
                elements.add(attends.getRegistration().getPerson());
            }
        }
        return elements;
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasStudent() && hasExecutionCourse()) {
            for (Registration registration : person.getStudent().getAllRegistrations()) {
                for (CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
                    if (curricularCourse.getDegree() == registration.getDegree()) {
                        return true;
                    }
                }
            }

            for (final Attends attends : getExecutionCourse().getAttendsSet()) {
                if (attends.getRegistration().getStudent() == person.getStudent()) {
                    return true;
                }
            }
        }

        return isDegreeCoordinator(person);
    }

    private boolean isDegreeCoordinator(final Person person) {
        if (person != null && person.hasAnyCoordinators()) {
            final ExecutionCourse executionCourse = getExecutionCourse();
            if (executionCourse != null) {
                final Set<ExecutionDegree> executionDegrees = executionCourse.getExecutionDegrees();
                for (final Coordinator coordinator : person.getCoordinatorsSet()) {
                    final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
                    if (executionDegrees.contains(executionDegree)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof DegreesOfExecutionCourseGroup;
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new DegreesOfExecutionCourseGroup((ExecutionCourse) arguments[0]);
            } catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.executionCourse.notExecutionCourse",
                        arguments[0].toString());
            }
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

    }

    @Override
    public PersistentStudentSharingDegreeOfExecutionCourseGroup convert() {
        return PersistentStudentSharingDegreeOfExecutionCourseGroup.getInstance(getExecutionCourse());
    }
}
