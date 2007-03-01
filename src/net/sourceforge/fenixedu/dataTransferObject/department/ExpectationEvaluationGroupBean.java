package net.sourceforge.fenixedu.dataTransferObject.department;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;

public class ExpectationEvaluationGroupBean implements Serializable {

    private DomainReference<ExecutionYear> executionYearReference;
    
    private DomainReference<Teacher> appraiserReference;
    
    private DomainReference<Teacher> evaluatedReference;
    
    public ExpectationEvaluationGroupBean(Teacher teacher, ExecutionYear executionYear) {
	setAppraiser(teacher);
	setExecutionYear(executionYear);
    }    
    
    public ExecutionYear getExecutionYear() {
	return (this.executionYearReference != null) ? this.executionYearReference.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYearReference = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }
    
    public Teacher getAppraiser() {
	return (this.appraiserReference != null) ? this.appraiserReference.getObject() : null;
    }

    public void setAppraiser(Teacher teacher) {
	this.appraiserReference = (teacher != null) ? new DomainReference<Teacher>(teacher) : null;
    }
    
    public Teacher getEvaluated() {
	return (this.evaluatedReference != null) ? this.evaluatedReference.getObject() : null;
    }

    public void setEvaluated(Teacher teacher) {
	this.evaluatedReference = (teacher != null) ? new DomainReference<Teacher>(teacher) : null;
    }
}
