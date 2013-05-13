package net.sourceforge.fenixedu.domain.accessControl;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;

public class CurricularCourseStudentsByExecutionPeriodGroup extends LeafGroup {
    private static final long serialVersionUID = 1L;

    private final CurricularCourse curricularCourseReference;

    private final ExecutionSemester executionPeriodReference;

    public CurricularCourseStudentsByExecutionPeriodGroup(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
        this.curricularCourseReference = curricularCourse;
        this.executionPeriodReference = executionSemester;
    }

    public CurricularCourse getCurricularCourse() {
        return this.curricularCourseReference;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionPeriodReference;
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        List<Enrolment> enrolments = getCurricularCourse().getEnrolmentsByExecutionPeriod(getExecutionPeriod());

        for (Enrolment enrolment : enrolments) {
            elements.add(enrolment.getStudentCurricularPlan().getRegistration().getPerson());
        }

        return super.freezeSet(elements);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getCurricularCourse()), new OidOperator(getExecutionPeriod()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            CurricularCourse course;
            ExecutionSemester period;

            try {
                course = (CurricularCourse) arguments[0];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(0, CurricularCourse.class, arguments[0].getClass());
            }

            try {
                period = (ExecutionSemester) arguments[1];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(1, ExecutionSemester.class, arguments[1].getClass());
            }

            return new CurricularCourseStudentsByExecutionPeriodGroup(course, period);
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
}
