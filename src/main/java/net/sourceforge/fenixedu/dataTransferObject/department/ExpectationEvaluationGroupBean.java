package net.sourceforge.fenixedu.dataTransferObject.department;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;

public class ExpectationEvaluationGroupBean implements Serializable {

    private ExecutionYear executionYearReference;

    private Teacher appraiserReference;

    private Teacher evaluatedReference;

    public ExpectationEvaluationGroupBean(Teacher teacher, ExecutionYear executionYear) {
        setAppraiser(teacher);
        setExecutionYear(executionYear);
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYearReference;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYearReference = executionYear;
    }

    public Teacher getAppraiser() {
        return this.appraiserReference;
    }

    public void setAppraiser(Teacher teacher) {
        this.appraiserReference = teacher;
    }

    public Teacher getEvaluated() {
        return this.evaluatedReference;
    }

    public void setEvaluated(Teacher teacher) {
        this.evaluatedReference = teacher;
    }
}
