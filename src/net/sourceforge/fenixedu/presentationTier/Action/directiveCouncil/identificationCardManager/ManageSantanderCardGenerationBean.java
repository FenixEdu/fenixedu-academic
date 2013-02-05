package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.identificationCardManager;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatch;

@SuppressWarnings("serial")
public class ManageSantanderCardGenerationBean implements Serializable {

    private ExecutionYear executionYear;
    private List<SantanderBatch> santanderBatches;
    private Boolean allowNewCreation;

    public ManageSantanderCardGenerationBean() {
        super();
        allowNewCreation = true;
    }

    public ManageSantanderCardGenerationBean(ExecutionYear executionYear) {
        this();
        setExecutionYear(executionYear);
    }

    public ManageSantanderCardGenerationBean(List<SantanderBatch> batches, ExecutionYear executionYear) {
        this();
        setSantanderBatches(batches);
        setExecutionYear(executionYear);
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public List<SantanderBatch> getSantanderBatches() {
        return santanderBatches;
    }

    public void setSantanderBatches(List<SantanderBatch> santanderBatches) {
        this.santanderBatches = santanderBatches;
    }

    public Boolean getAllowNewCreation() {
        return allowNewCreation;
    }

    public void setAllowNewCreation(Boolean allowNewCreation) {
        this.allowNewCreation = allowNewCreation;
    }

}
