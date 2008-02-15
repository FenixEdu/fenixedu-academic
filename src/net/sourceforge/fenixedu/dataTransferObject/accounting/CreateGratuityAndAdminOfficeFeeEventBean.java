package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class CreateGratuityAndAdminOfficeFeeEventBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8828406937369185342L;

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private DomainReference<ExecutionYear> executionYear;

    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		studentCurricularPlan) : null;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }
    
    public CreateGratuityAndAdminOfficeFeeEventBean(final StudentCurricularPlan studentCurricularPlan){
	setStudentCurricularPlan(studentCurricularPlan);
    }

}
