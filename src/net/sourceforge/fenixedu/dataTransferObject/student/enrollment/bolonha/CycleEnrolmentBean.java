package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CycleEnrolmentBean implements Serializable {

    private static final long serialVersionUID = -7926077929745839701L;

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private DomainReference<ExecutionSemester> executionSemester;

    private DomainReference<CycleCourseGroup> cycleCourseGroupToEnrol;

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
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		studentCurricularPlan) : null;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester != null) ? this.executionSemester.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
    }

    public CycleCourseGroup getCycleCourseGroupToEnrol() {
	return (this.cycleCourseGroupToEnrol != null) ? this.cycleCourseGroupToEnrol.getObject() : null;
    }

    public void setCycleCourseGroupToEnrol(CycleCourseGroup cycleCourseGroup) {
	this.cycleCourseGroupToEnrol = (cycleCourseGroup != null) ? new DomainReference<CycleCourseGroup>(cycleCourseGroup)
		: null;
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

    public List<CycleCourseGroup> getCycleDestinationAffinities() {
	final List<CycleCourseGroup> affinities = getDegreeCurricularPlan().getDestinationAffinities(getSourceCycleAffinity());
	
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
	return AccessControl.getUserView().hasRoleType(RoleType.STUDENT);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan() {
	return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public CycleCurriculumGroup getSourceCycle() {
	return getStudentCurricularPlan().getCycle(getSourceCycleAffinity());
    }

}
