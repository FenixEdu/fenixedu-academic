package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pt.ist.bennu.core.security.Authenticate;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CycleEnrolmentBean implements Serializable {

    private static final long serialVersionUID = -7926077929745839701L;

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionSemester executionSemester;

    private CycleCourseGroup cycleCourseGroupToEnrol;

    private CycleType sourceCycleAffinity;

    private CycleType cycleTypeToEnrol;

    public CycleEnrolmentBean(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
            final CycleType sourceCycleAffinity, final CycleType cycleTypeToEnrol) {
        setStudentCurricularPlan(studentCurricularPlan);
        setExecutionPeriod(executionSemester);
        setSourceCycleAffinity(sourceCycleAffinity);
        setCycleTypeToEnrol(cycleTypeToEnrol);
    }

    public CycleEnrolmentBean(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
            final CycleCourseGroup cycleCourseGroup) {
        this(studentCurricularPlan, executionSemester, CycleType.FIRST_CYCLE, cycleCourseGroup.getCycleType());
        setCycleCourseGroupToEnrol(cycleCourseGroup);
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public CycleCourseGroup getCycleCourseGroupToEnrol() {
        return this.cycleCourseGroupToEnrol;
    }

    public void setCycleCourseGroupToEnrol(CycleCourseGroup cycleCourseGroup) {
        this.cycleCourseGroupToEnrol = cycleCourseGroup;
    }

    public CycleType getSourceCycleAffinity() {
        return sourceCycleAffinity;
    }

    public void setSourceCycleAffinity(CycleType sourceCycleAffinity) {
        this.sourceCycleAffinity = sourceCycleAffinity;
    }

    public CycleType getCycleTypeToEnrol() {
        return cycleTypeToEnrol;
    }

    public void setCycleTypeToEnrol(CycleType cycleTypeToEnrol) {
        this.cycleTypeToEnrol = cycleTypeToEnrol;
    }

    public Collection<CycleCourseGroup> getCycleDestinationAffinities() {
        final Collection<CycleCourseGroup> affinities =
                getDegreeCurricularPlan().getDestinationAffinities(getSourceCycleAffinity());

        if (affinities.isEmpty()) {
            return Collections.emptyList();
        }

        if (!isStudent()) {
            return affinities;
        }

        final List<CycleCourseGroup> result = new ArrayList<CycleCourseGroup>();
        for (final CycleCourseGroup cycleCourseGroup : affinities) {
            final DegreeCurricularPlan degreeCurricularPlan = cycleCourseGroup.getParentDegreeCurricularPlan();
            if (degreeCurricularPlan.hasEnrolmentPeriodInCurricularCourses(getExecutionPeriod())) {
                result.add(cycleCourseGroup);
            }
        }
        return result;
    }

    private boolean isStudent() {
        return Authenticate.getUser().getPerson().hasRole(RoleType.STUDENT);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan() {
        return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public CycleCurriculumGroup getSourceCycle() {
        return getStudentCurricularPlan().getCycle(getSourceCycleAffinity());
    }

}
