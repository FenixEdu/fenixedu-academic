package net.sourceforge.fenixedu.domain.accessControl;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;

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

}
