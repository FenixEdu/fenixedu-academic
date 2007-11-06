package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;

public class ExecutionYearIntervalBean extends ExecutionYearBean implements Serializable {

    private DomainReference<ExecutionYear> finalExecutionYear;
    private ResultPublicationType publicationType;

    public ExecutionYearIntervalBean() {
	setFirstExecutionYear(ExecutionYear.readFirstExecutionYear());
	setFinalExecutionYear(ExecutionYear.readLastExecutionYear());
    }

    public ExecutionYearIntervalBean(ExecutionYear firstExecutionYear, ExecutionYear finalExecutionYear) {
	setFirstExecutionYear(firstExecutionYear);
	setFinalExecutionYear(finalExecutionYear);
    }

    public void setFirstExecutionYear(ExecutionYear executionYear) {
	this.setExecutionYear(executionYear);
    }

    public ExecutionYear getFirstExecutionYear() {
	return this.getExecutionYear();
    }

    public void setFinalExecutionYear(ExecutionYear executionYear) {
	this.finalExecutionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    public ExecutionYear getFinalExecutionYear() {
	return this.finalExecutionYear.getObject();
    }
    
    public ExecutionYear getFirstNonNullExecutionYear() {
	return getFirstExecutionYear() != null ? getFirstExecutionYear() : ExecutionYear.readFirstExecutionYear();
    }

    public ExecutionYear getFinalNonNullExecutionYear() {
	return getFinalExecutionYear() != null ? getFinalExecutionYear() : ExecutionYear.readLastExecutionYear();
    }

    public ResultPublicationType getPublicationType() {
        return publicationType;
    }

    public void setPublicationType(ResultPublicationType publicationType) {
        this.publicationType = publicationType;
    }
   
}
