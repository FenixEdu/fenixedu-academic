package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class SpecialSeasonCodeBean implements Serializable {

    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionSemester executionSemester;
    private SpecialSeasonCode specialSeasonCode;

    public SpecialSeasonCodeBean(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionSemester);
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

    public SpecialSeasonCode getSpecialSeasonCode() {
	return this.specialSeasonCode;
    }

    public void setSpecialSeasonCode(SpecialSeasonCode specialSeasonCode) {
	this.specialSeasonCode = specialSeasonCode;
    }

}
