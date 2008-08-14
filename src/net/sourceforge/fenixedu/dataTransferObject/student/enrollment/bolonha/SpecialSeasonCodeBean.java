package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class SpecialSeasonCodeBean implements Serializable {

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;
    private DomainReference<ExecutionSemester> executionSemester;
    private DomainReference<SpecialSeasonCode> specialSeasonCode;

    public SpecialSeasonCodeBean(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionSemester);
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

    public SpecialSeasonCode getSpecialSeasonCode() {
	return (this.specialSeasonCode != null) ? this.specialSeasonCode.getObject() : null;
    }

    public void setSpecialSeasonCode(SpecialSeasonCode specialSeasonCode) {
	this.specialSeasonCode = (specialSeasonCode != null) ? new DomainReference<SpecialSeasonCode>(specialSeasonCode) : null;
    }

}
