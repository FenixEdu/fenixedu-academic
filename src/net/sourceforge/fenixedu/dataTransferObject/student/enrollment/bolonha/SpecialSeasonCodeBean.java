package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

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
    
    public boolean isStudentAuthorized() {
	List<StudentStatute> statutes = studentCurricularPlan.getRegistration().getStudent().getStudentStatutes();
	for(StudentStatute statute : statutes) {
	    if(statute.getStatuteType().isSpecialSeasonGranted())
		return true;
	}
	return false;
    }

}
