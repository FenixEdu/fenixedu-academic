package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;

public class SearchPhdIndividualProgramProcessBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5653277152319382139L;

    private DomainReference<ExecutionYear> executionYear;

    private PhdIndividualProgramProcessState processState;

    private String processNumber;

    private Integer studentNumber;

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public PhdIndividualProgramProcessState getProcessState() {
	return processState;
    }

    public void setProcessState(PhdIndividualProgramProcessState processState) {
	this.processState = processState;
    }

    public String getProcessNumber() {
	return processNumber;
    }

    public void setProcessNumber(String processNumber) {
	this.processNumber = processNumber;
    }

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

}
