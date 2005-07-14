/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeThesisDataVersion extends MasterDegreeThesisDataVersion_Base {

    public MasterDegreeThesisDataVersion() {
    }

    public MasterDegreeThesisDataVersion(IMasterDegreeThesis masterDegreeThesis,
            IEmployee responsibleEmployee, String dissertationTitle, Timestamp lastModification,
            State currentState) {
        this.setMasterDegreeThesis(masterDegreeThesis);
        this.setResponsibleEmployee(responsibleEmployee);
        this.setDissertationTitle(dissertationTitle);
        this.setLastModification(lastModification);
        this.setCurrentState(currentState);
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + this.getIdInternal() + "; \n";
        result += "masterDegreeThesis = " + this.getMasterDegreeThesis().getIdInternal() + "; \n";
        result += "externalAssistentGuiders = " + this.getExternalAssistentGuiders().toString() + "; \n";
        result += "assistentGuiders = " + this.getAssistentGuiders().toString() + "; \n";
        result += "guiders = " + this.getGuiders().toString() + "; \n";
        result += "responsibleEmployee = " + this.getResponsibleEmployee().getIdInternal() + "; \n";
        result += "dissertationTitle = " + this.getDissertationTitle().toString() + "; \n";
        result += "lastModification = " + this.getLastModification().toString() + "; \n";
        result += "currentState = " + this.getCurrentState().toString() + "; \n";
        result += "] \n";

        return result;
    }

}
