package net.sourceforge.fenixedu.domain.accessControl;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

public class CurricularCourseStudentsByExecutionPeriodGroup extends LeafGroup {
    private static final long serialVersionUID = 1L;

    private DomainReference<CurricularCourse> curricularCourseReference;

    private DomainReference<ExecutionPeriod> executionPeriodReference;

    public CurricularCourseStudentsByExecutionPeriodGroup(CurricularCourse curricularCourse,
            ExecutionPeriod executionPeriod) {
        this.curricularCourseReference = new DomainReference<CurricularCourse>(curricularCourse);
        this.executionPeriodReference = new DomainReference<ExecutionPeriod>(executionPeriod);
    }

    public CurricularCourse getCurricularCourse() {
        return this.curricularCourseReference.getObject();
    }

    public ExecutionPeriod getExecutionPeriod() {
        return this.executionPeriodReference.getObject();
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        List<Enrolment> enrolments = getCurricularCourse().getEnrolmentsByExecutionPeriod(
                getExecutionPeriod());

        for (Enrolment enrolment : enrolments) {
            elements.add(enrolment.getStudentCurricularPlan().getRegistration().getPerson());
        }

        return super.freezeSet(elements);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] {
                new IdOperator(getCurricularCourse()),
                new IdOperator(getExecutionPeriod())
        };
    }

    public static class Builder implements GroupBuilder {

        public Group build(Object[] arguments) {
            CurricularCourse course;
            ExecutionPeriod period;

            try {
                course = (CurricularCourse) arguments[0];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(0, CurricularCourse.class, arguments[0].getClass());
            }
            
            try {
                period = (ExecutionPeriod) arguments[1];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(1, ExecutionPeriod.class, arguments[1].getClass());
            }
            
            return new CurricularCourseStudentsByExecutionPeriodGroup(course, period);
        }

        public int getMinArguments() {
            return 2;
        }

        public int getMaxArguments() {
            return 2;
        }
        
    }
}
