/*
 * Created on Oct 14, 2003
 *
 */
package DataBeans;

import java.sql.Timestamp;
import java.util.List;

import Dominio.IMasterDegreeThesisDataVersion;
import Util.State;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class InfoMasterDegreeThesisDataVersion extends InfoObject {

    private InfoMasterDegreeThesis infoMasterDegreeThesis;

    private List infoExternalAssistentGuiders;

    private List infoAssistentGuiders;

    private List infoGuiders;

    private List infoExternalGuiders;

    private InfoEmployee infoResponsibleEmployee;

    private String dissertationTitle;

    private Timestamp lastModification;

    private State currentState;

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setDissertationTitle(String dissertationTitle) {
        this.dissertationTitle = dissertationTitle;
    }

    public String getDissertationTitle() {
        return dissertationTitle;
    }

    public void setInfoAssistentGuiders(List infoAssistentGuiders) {
        this.infoAssistentGuiders = infoAssistentGuiders;
    }

    public List getInfoAssistentGuiders() {
        return infoAssistentGuiders;
    }

    public void setInfoExternalAssistentGuiders(List infoExternalAssistentGuiders) {
        this.infoExternalAssistentGuiders = infoExternalAssistentGuiders;
    }

    public List getInfoExternalAssistentGuiders() {
        return infoExternalAssistentGuiders;
    }

    public void setInfoGuiders(List infoGuiders) {
        this.infoGuiders = infoGuiders;
    }

    public List getInfoGuiders() {
        return infoGuiders;
    }

    public List getInfoExternalGuiders() {
        return infoExternalGuiders;
    }

    public void setInfoExternalGuiders(List infoExternalGuiders) {
        this.infoExternalGuiders = infoExternalGuiders;
    }

    public void setInfoMasterDegreeThesis(InfoMasterDegreeThesis infoMasterDegreeThesis) {
        this.infoMasterDegreeThesis = infoMasterDegreeThesis;
    }

    public InfoMasterDegreeThesis getInfoMasterDegreeThesis() {
        return infoMasterDegreeThesis;
    }

    public void setInfoResponsibleEmployee(InfoEmployee infoResponsibleEmployee) {
        this.infoResponsibleEmployee = infoResponsibleEmployee;
    }

    public InfoEmployee getInfoResponsibleEmployee() {
        return infoResponsibleEmployee;
    }

    public void setLastModification(Timestamp lastModification) {
        this.lastModification = lastModification;
    }

    public Timestamp getLastModification() {
        return lastModification;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "infoMasterDegreeThesis = " + this.infoMasterDegreeThesis.getIdInternal() + "; \n";
        result += "infoExternalAssistentGuiders = " + this.infoExternalAssistentGuiders.toString()
                + "; \n";
        result += "infoAssistentGuiders = " + this.infoAssistentGuiders.toString() + "; \n";
        result += "infoGuiders" + this.infoGuiders.toString() + "; \n";
        result += "infoResponsibleEmployee = " + this.infoResponsibleEmployee.getIdInternal() + "; \n";
        result += "dissertationTitle = " + this.dissertationTitle.toString() + "; \n";
        result += "lastModification = " + this.lastModification.toString() + "; \n";
        result += "currentState = " + this.currentState.toString() + "; \n";
        result += "] \n";

        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof InfoMasterDegreeThesisDataVersion) {
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) obj;
            result = this.infoMasterDegreeThesis.equals(infoMasterDegreeThesisDataVersion
                    .getInfoMasterDegreeThesis())
                    && this.lastModification.equals(infoMasterDegreeThesisDataVersion
                            .getLastModification());
        }
        return result;
    }

    public static InfoMasterDegreeThesisDataVersion newInfoFromDomain(
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;
        if (masterDegreeThesisDataVersion != null) {
            infoMasterDegreeThesisDataVersion = new InfoMasterDegreeThesisDataVersion();
            infoMasterDegreeThesisDataVersion.copyFromDomain(masterDegreeThesisDataVersion);
        }
        return infoMasterDegreeThesisDataVersion;
    }

    public void copyFromDomain(IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        super.copyFromDomain(masterDegreeThesisDataVersion);
        if (masterDegreeThesisDataVersion != null) {
            setCurrentState(masterDegreeThesisDataVersion.getCurrentState());
            setDissertationTitle(masterDegreeThesisDataVersion.getDissertationTitle());
            setLastModification(masterDegreeThesisDataVersion.getLastModification());
        }
    }

}