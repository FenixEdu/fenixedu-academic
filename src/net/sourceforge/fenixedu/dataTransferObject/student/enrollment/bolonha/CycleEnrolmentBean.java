package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

public class CycleEnrolmentBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7926077929745839701L;

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private DomainReference<ExecutionPeriod> executionPeriod;

    private DomainReference<CycleCourseGroup> cycleCourseGroupToEnrol;

    private CycleType sourceCycleAffinity;

    private CycleType cycleTypeToEnrol;

    public CycleEnrolmentBean(final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod,
	    final CycleType sourceCycleAffinity, final CycleType cycleTypeToEnrol) {
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionPeriod);
	setSourceCycleAffinity(sourceCycleAffinity);
	setCycleTypeToEnrol(cycleTypeToEnrol);
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		studentCurricularPlan) : null;
    }

    public ExecutionPeriod getExecutionPeriod() {
	return (this.executionPeriod != null) ? this.executionPeriod.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
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
	return getStudentCurricularPlan().getDegreeCurricularPlan().getRoot().getCycleCourseGroup(getSourceCycleAffinity())
		.getDestinationAffinities();
    }

}
