/*
 * Created on Oct 10, 2003
 *
 *
 */
package Dominio;

import java.sql.Timestamp;
import java.util.List;

import Util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeThesisDataVersion extends DomainObject implements
        IMasterDegreeThesisDataVersion {

    //database internal keys
    private Integer keyMasterDegreeThesis;

    private Integer keyEmployee;

    //fields
    private IMasterDegreeThesis masterDegreeThesis;

    private List externalAssistentGuiders;

    private List assistentGuiders;

    private List guiders;

    private List externalGuiders;

    private IEmployee responsibleEmployee;

    private String dissertationTitle;

    private Timestamp lastModification;

    private State currentState;

    public MasterDegreeThesisDataVersion() {
    }

    public MasterDegreeThesisDataVersion(IMasterDegreeThesis masterDegreeThesis,
            IEmployee responsibleEmployee, String dissertationTitle, Timestamp lastModification,
            State currentState) {
        this.masterDegreeThesis = masterDegreeThesis;
        this.responsibleEmployee = responsibleEmployee;
        this.dissertationTitle = dissertationTitle;
        this.lastModification = lastModification;
        this.currentState = currentState;
    }

    /**
     * @param masterDegreeThesis
     * @param externalAssistentGuiders
     * @param assistentGuiders
     * @param guiders
     * @param responsibleEmployee
     * @param dissertationTitle
     * @param lastModification
     * @param currentState
     */
    public MasterDegreeThesisDataVersion(IMasterDegreeThesis masterDegreeThesis,
            List externalAssistentGuiders, List assistentGuiders, List guiders,
            IEmployee responsibleEmployee, String dissertationTitle, Timestamp lastModification,
            State currentState) {
        this.masterDegreeThesis = masterDegreeThesis;
        this.externalAssistentGuiders = externalAssistentGuiders;
        this.assistentGuiders = assistentGuiders;
        this.guiders = guiders;
        this.responsibleEmployee = responsibleEmployee;
        this.dissertationTitle = dissertationTitle;
        this.lastModification = lastModification;
        this.currentState = currentState;
    }

    public void setAssistentGuiders(List assistentGuiders) {
        this.assistentGuiders = assistentGuiders;
    }

    public List getAssistentGuiders() {
        return assistentGuiders;
    }

    public void setDissertationTitle(String dissertationTitle) {
        this.dissertationTitle = dissertationTitle;
    }

    public String getDissertationTitle() {
        return dissertationTitle;
    }

    public void setExternalAssistentGuiders(List externalAssistentGuiders) {
        this.externalAssistentGuiders = externalAssistentGuiders;
    }

    public List getExternalAssistentGuiders() {
        return externalAssistentGuiders;
    }

    public void setGuiders(List guiders) {
        this.guiders = guiders;
    }

    public List getGuiders() {
        return guiders;
    }

    public List getExternalGuiders() {
        return externalGuiders;
    }

    public void setExternalGuiders(List externalGuiders) {
        this.externalGuiders = externalGuiders;
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

    public void setKeyEmployee(Integer keyEmployee) {
        this.keyEmployee = keyEmployee;
    }

    public Integer getKeyEmployee() {
        return keyEmployee;
    }

    public void setResponsibleEmployee(IEmployee responsibleEmployee) {
        this.responsibleEmployee = responsibleEmployee;
    }

    public IEmployee getResponsibleEmployee() {
        return responsibleEmployee;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "masterDegreeThesis = " + this.masterDegreeThesis.getIdInternal() + "; \n";
        result += "externalAssistentGuiders = " + this.externalAssistentGuiders.toString() + "; \n";
        result += "assistentGuiders = " + this.assistentGuiders.toString() + "; \n";
        result += "guiders = " + this.guiders.toString() + "; \n";
        result += "responsibleEmployee = " + this.responsibleEmployee.getIdInternal() + "; \n";
        result += "dissertationTitle = " + this.dissertationTitle.toString() + "; \n";
        result += "lastModification = " + this.lastModification.toString() + "; \n";
        result += "currentState = " + this.currentState.toString() + "; \n";
        result += "] \n";

        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof IMasterDegreeThesisDataVersion) {
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) obj;
            result = this.masterDegreeThesis.equals(masterDegreeThesisDataVersion
                    .getMasterDegreeThesis())
                    && this.lastModification.equals(masterDegreeThesisDataVersion.getLastModification());
        }

        return result;
    }

}