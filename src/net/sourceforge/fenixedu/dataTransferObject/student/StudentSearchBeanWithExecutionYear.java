package net.sourceforge.fenixedu.dataTransferObject.student;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class StudentSearchBeanWithExecutionYear extends StudentsSearchBean {

    private DomainReference<ExecutionYear> executionYearDomainReference;

    public ExecutionYear getExecutionYear() {
	return executionYearDomainReference == null ? null : executionYearDomainReference.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYearDomainReference = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
    }

}
