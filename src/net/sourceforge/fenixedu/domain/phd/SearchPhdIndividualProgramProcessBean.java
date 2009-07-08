package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class SearchPhdIndividualProgramProcessBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5653277152319382139L;

    private DomainReference<ExecutionYear> executionYear;

    private PhdIndividualProgramProcessState processState;

    private String processNumber;

    private Integer studentNumber;

    private List<DomainReference<PhdProgram>> phdPrograms;

    private Boolean filterPhdPrograms = Boolean.TRUE;

    public Boolean getFilterPhdPrograms() {
	return filterPhdPrograms;
    }

    public void setFilterPhdPrograms(Boolean filterPhdPrograms) {
	this.filterPhdPrograms = filterPhdPrograms;
    }

    public List<PhdProgram> getPhdPrograms() {
	final List<PhdProgram> result = new ArrayList<PhdProgram>();
	for (final DomainReference<PhdProgram> each : this.phdPrograms) {
	    result.add(each.getObject());
	}

	return result;
    }

    public void setPhdPrograms(List<PhdProgram> phdPrograms) {
	setPhdPrograms((Collection<PhdProgram>) phdPrograms);
    }

    public void setPhdPrograms(Collection<PhdProgram> phdPrograms) {
	final List<DomainReference<PhdProgram>> result = new ArrayList<DomainReference<PhdProgram>>();
	for (final PhdProgram each : phdPrograms) {
	    result.add(new DomainReference<PhdProgram>(each));
	}

	this.phdPrograms = result;
    }

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
