/*
 * Created on Oct 10, 2003
 *
 */
package Dominio;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import Util.MasterDegreeClassification;
import Util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeProofVersion extends DomainObject implements IMasterDegreeProofVersion {

    //database internal keys
    private Integer keyMasterDegreeThesis;

    private Integer keyEmployee;

    //fields
    private IMasterDegreeThesis masterDegreeThesis;

    private IEmployee responsibleEmployee;

    private Timestamp lastModification;

    private Date proofDate;

    private Date thesisDeliveryDate;

    private MasterDegreeClassification finalResult;

    private Integer attachedCopiesNumber;

    private State currentState;

    private List juries; /* Teachers */

    private List externalJuries; /* External Persons */

    /**
     * Default Constructor
     */
    public MasterDegreeProofVersion() {

    }

    /**
     * @param masterDegreeThesis
     * @param responsibleEmployee
     * @param lastModification
     * @param proofDate
     * @param thesisDeliveryDate
     * @param finalResult
     * @param attachedCopiesNumber
     * @param currentState
     */
    public MasterDegreeProofVersion(IMasterDegreeThesis masterDegreeThesis,
            IEmployee responsibleEmployee, Timestamp lastModification, Date proofDate,
            Date thesisDeliveryDate, MasterDegreeClassification finalResult,
            Integer attachedCopiesNumber, State currentState, List juries, List externalJuries) {
        this.masterDegreeThesis = masterDegreeThesis;
        this.responsibleEmployee = responsibleEmployee;
        this.lastModification = lastModification;
        this.proofDate = proofDate;
        this.thesisDeliveryDate = thesisDeliveryDate;
        this.finalResult = finalResult;
        this.attachedCopiesNumber = attachedCopiesNumber;
        this.currentState = currentState;
        this.juries = juries;
        this.externalJuries = externalJuries;
    }

    public void setLastModification(Timestamp lastModification) {
        this.lastModification = lastModification;
    }

    public Timestamp getLastModification() {
        return lastModification;
    }

    public void setMasterDegreeThesis(IMasterDegreeThesis masterDegreeThesis) {
        this.masterDegreeThesis = masterDegreeThesis;
    }

    public IMasterDegreeThesis getMasterDegreeThesis() {
        return masterDegreeThesis;
    }

    public void setKeyMasterDegreeThesis(Integer keyMasterDegreeThesis) {
        this.keyMasterDegreeThesis = keyMasterDegreeThesis;
    }

    public Integer getKeyMasterDegreeThesis() {
        return keyMasterDegreeThesis;
    }

    public void setProofDate(Date proofDate) {
        this.proofDate = proofDate;
    }

    public Date getProofDate() {
        return proofDate;
    }

    public void setThesisDeliveryDate(Date thesisDeliveryDate) {
        this.thesisDeliveryDate = thesisDeliveryDate;
    }

    public Date getThesisDeliveryDate() {
        return thesisDeliveryDate;
    }

    public void setFinalResult(MasterDegreeClassification finalResult) {
        this.finalResult = finalResult;
    }

    public MasterDegreeClassification getFinalResult() {
        return finalResult;
    }

    public void setAttachedCopiesNumber(Integer attachedCopiesNumber) {
        this.attachedCopiesNumber = attachedCopiesNumber;
    }

    public Integer getAttachedCopiesNumber() {
        return attachedCopiesNumber;
    }

    public void setResponsibleEmployee(IEmployee responsibleEmployee) {
        this.responsibleEmployee = responsibleEmployee;
    }

    public IEmployee getResponsibleEmployee() {
        return responsibleEmployee;
    }

    public void setKeyEmployee(Integer keyEmployee) {
        this.keyEmployee = keyEmployee;
    }

    public Integer getKeyEmployee() {
        return keyEmployee;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setJuries(List juries) {
        this.juries = juries;
    }

    public List getJuries() {
        return juries;
    }

    public List getExternalJuries() {
        return externalJuries;
    }

    public void setExternalJuries(List externalJuries) {
        this.externalJuries = externalJuries;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "masterDegreeThesis = " + this.masterDegreeThesis.getIdInternal() + "; \n";
        result += "responsibleEmployee = " + this.responsibleEmployee.getIdInternal() + "; \n";
        result += "lastModification = " + this.lastModification.toString() + "; \n";
        result += "proofDate = " + this.proofDate.toString() + "; \n";
        result += "thesisDeliveryDate = " + this.thesisDeliveryDate.toString() + "; \n";
        result += "finalResult = " + this.finalResult.toString() + "; \n";
        result += "attachedCopiesNumber = " + this.attachedCopiesNumber.toString() + "; \n";
        result += "currentState = " + this.currentState.toString() + "; \n";
        result += "] \n";

        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof IMasterDegreeProofVersion) {
            IMasterDegreeProofVersion masterDegreeProofVersion = (IMasterDegreeProofVersion) obj;
            result = this.masterDegreeThesis.equals(masterDegreeProofVersion.getMasterDegreeThesis())
                    && this.lastModification.equals(masterDegreeProofVersion.getLastModification());
        }

        return result;
    }
}