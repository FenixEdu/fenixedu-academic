package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;

import org.apache.commons.collections.CollectionUtils;

public class ExecutionCourseTeachersGroup extends AbstractExecutionCourseTeachersGroup {

    private static final long serialVersionUID = 1L;

    public ExecutionCourseTeachersGroup(ExecutionCourse executionCourse) {
        super(executionCourse);
    }

    @Override
    public String getPresentationNameKey() {
        return "label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroupWithName";
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new ExecutionCourseTeachersGroup((ExecutionCourse) arguments[0]);
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
    public Set<Person> getElements() {
        final Set<Person> elements = super.buildSet();
        if (hasExecutionCourse()) {
            final Collection<Professorship> professorships = getProfessorships();
            final Collection<Person> persons = CollectionUtils.collect(professorships, new ProfessorshipPersonTransformer());
            elements.addAll(persons);
            for (ExecutionDegree executionDegree : getExecutionCourse().getExecutionDegrees()) {
                for (Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    elements.add(coordinator.getPerson());
                }
            }
        }

        return super.freezeSet(elements);
    }

    @Override
    public Collection<Professorship> getProfessorships() {
        return getExecutionCourse().getProfessorships();
    }

    @Override
    public boolean isMember(final Person person) {
        return super.isMember(person) || isDegreeCoordinator(person);
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
    public PersistentTeacherGroup convert() {
        return PersistentTeacherGroup.getInstance(getExecutionCourse());
    }
}
