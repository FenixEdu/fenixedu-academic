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
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ExecutionCourseTeachersGroup extends AbstractExecutionCourseTeachersGroup {

    private static final long serialVersionUID = 1L;

    public ExecutionCourseTeachersGroup(ExecutionCourse executionCourse) {
	super(executionCourse);
    }

    @Override
    public String getName() {
	return RenderUtils.getResourceString("SITE_RESOURCES",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroupWithName",
		new Object[] { getExecutionCourse().getNome() });
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    try {
		return new ExecutionCourseTeachersGroup((ExecutionCourse) arguments[0]);
	    } catch (ClassCastException e) {
		throw new GroupDynamicExpressionException("accessControl.group.builder.executionCourse.notExecutionCourse",
			arguments[0].toString());
	    }
	}

	public int getMinArguments() {
	    return 0;
	}

	public int getMaxArguments() {
	    return 1;
	}

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
	if (person.hasAnyCoordinators()) {
	    final ExecutionCourse executionCourse = getExecutionCourse();
	    final Set<ExecutionDegree> executionDegrees = executionCourse.getExecutionDegrees();
	    for (final Coordinator coordinator : person.getCoordinatorsSet()) {
		final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
		if (executionDegrees.contains(executionDegree)) {
		    return true;
		}
	    }
	}
	return false;
    }

}
