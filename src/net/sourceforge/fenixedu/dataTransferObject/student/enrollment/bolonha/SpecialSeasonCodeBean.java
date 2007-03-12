package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class SpecialSeasonCodeBean implements Serializable{
    
    private DomainReference<StudentCurricularPlan> studentCurricularPlan;
    private DomainReference<ExecutionPeriod> executionPeriod;
    private DomainReference<SpecialSeasonCode> specialSeasonCode;
    
    public SpecialSeasonCodeBean(final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod) {
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionPeriod);
    }
    
    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(studentCurricularPlan) : null;
    }

    public ExecutionPeriod getExecutionPeriod() {
	return (this.executionPeriod != null) ? this.executionPeriod.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
    }

    public SpecialSeasonCode getSpecialSeasonCode() {
	return (this.specialSeasonCode != null) ? this.specialSeasonCode.getObject() : null;
    }

    public void setSpecialSeasonCode(SpecialSeasonCode specialSeasonCode) {
	this.specialSeasonCode = (specialSeasonCode != null) ? new DomainReference<SpecialSeasonCode>(specialSeasonCode) : null;
    }




}
