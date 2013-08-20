package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DelegateCurricularCourseStudentsGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final String curricularCourseId;

    private final String executionYearId;

    public DelegateCurricularCourseStudentsGroup(CurricularCourse curricularCourse, ExecutionYear executionYear) {
        curricularCourseId = curricularCourse.getExternalId();
        executionYearId = executionYear.getExternalId();
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
        return new Argument[] { new OidOperator(getCurricularCourse()), new OidOperator(getExecutionYear()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new DelegateCurricularCourseStudentsGroup((CurricularCourse) arguments[0], (ExecutionYear) arguments[1]);
            } catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.executionCourse.notExecutionCourse",
                        arguments[0].toString());
            }
        }

        @Override
        public int getMinArguments() {
            return 2;
        }

        @Override
        public int getMaxArguments() {
            return 2;
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
        return getCurricularCourse().getName() + " - " + getNumberOfEnrolledStudents() + " " + super.getName();
    }

    @Override
    public boolean hasPresentationNameDynamic() {
        return true;
    }

    @Override
    public String getPresentationNameBundle() {
        return "resources.DelegateResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label.enrolledStudents";
    }

    public ExecutionYear getExecutionYear() {
        return AbstractDomainObject.fromExternalId(executionYearId);
    }

    public CurricularCourse getCurricularCourse() {
        return (CurricularCourse) (curricularCourseId != null ? AbstractDomainObject.fromExternalId(curricularCourseId) : null);
    }

    private int getNumberOfEnrolledStudents() {
        final CurricularCourse curricularCourse = getCurricularCourse();
        final ExecutionYear executionYear = getExecutionYear();
        return curricularCourse.getEnrolmentsByExecutionYear(executionYear).size();
    }

}
