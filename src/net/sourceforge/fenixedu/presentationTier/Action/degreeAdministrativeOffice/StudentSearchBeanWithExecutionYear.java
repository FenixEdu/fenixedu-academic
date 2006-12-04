package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.StudentsSearchBean;

public class StudentSearchBeanWithExecutionYear extends StudentsSearchBean {

    private DomainReference<ExecutionYear> executionYearDomainReference;

    public ExecutionYear getExecutionYear() {
        return executionYearDomainReference == null ? null : executionYearDomainReference.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYearDomainReference = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
    }

}
