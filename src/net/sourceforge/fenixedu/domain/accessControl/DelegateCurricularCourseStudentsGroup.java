package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class DelegateCurricularCourseStudentsGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final DomainReference<CurricularCourse> curricularCourse;

    private final DomainReference<ExecutionYear> executionYear;

    public DelegateCurricularCourseStudentsGroup(CurricularCourse curricularCourse, ExecutionYear executionYear) {
	this.curricularCourse = new DomainReference<CurricularCourse>(curricularCourse);
	this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> people = new HashSet<Person>();

	final CurricularCourse curricularCourse = getCurricularCourse();
	final ExecutionYear executionYear = getExecutionYear();
	for (Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionYear(executionYear)) {
	    Registration registration = enrolment.getRegistration();
	    people.add(registration.getPerson());
	}

	return people;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getCurricularCourse()), new IdOperator(getExecutionYear()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    try {
		return new DelegateCurricularCourseStudentsGroup((CurricularCourse) arguments[0], (ExecutionYear) arguments[1]);
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
    public boolean isMember(Person person) {
	if (person.hasStudent()) {
	    if (getElements().contains(person.getStudent())) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public String getName() {
	return getCurricularCourse().getName() + " - " + getNumberOfEnrolledStudents() + " "
		+ RenderUtils.getResourceString("DELEGATES_RESOURCES", "label.enrolledStudents");
    }

    public ExecutionYear getExecutionYear() {
	return (executionYear != null ? executionYear.getObject() : null);
    }

    public CurricularCourse getCurricularCourse() {
	return (curricularCourse != null ? curricularCourse.getObject() : null);
    }

    private int getNumberOfEnrolledStudents() {
	final CurricularCourse curricularCourse = getCurricularCourse();
	final ExecutionYear executionYear = getExecutionYear();
	return curricularCourse.getEnrolmentsByExecutionYear(executionYear).size();
    }

}
