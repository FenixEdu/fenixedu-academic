package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionDegree;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionSemester;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionYear;

public class AddAttendsBean implements Serializable, HasExecutionYear, HasExecutionSemester, HasExecutionDegree {

    private DomainReference<ExecutionYear> executionYear;
    private DomainReference<ExecutionSemester> executionPeriod;
    private DomainReference<ExecutionDegree> executionDegree;
    private DomainReference<ExecutionCourse> executionCourse;

    public ExecutionYear getExecutionYear() {
	return executionYear == null ? null : executionYear.getObject();
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
	this.executionYear = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
    }

    public ExecutionSemester getExecutionPeriod() {
	return executionPeriod == null ? null : executionPeriod.getObject();
    }

    public void setExecutionPeriod(ExecutionSemester executionPeriod) {
	this.executionPeriod = executionPeriod == null ? null : new DomainReference<ExecutionSemester>(executionPeriod);
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree == null ? null : executionDegree.getObject();
    }

    public void setExecutionDegree(final ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree == null ? null : new DomainReference<ExecutionDegree>(executionDegree);
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse == null ? null : executionCourse.getObject();
    }

    public void setExecutionCourse(final ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse == null ? null : new DomainReference<ExecutionCourse>(executionCourse);
    }

}
