/*
 * Created on Oct 10, 2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeProofVersion extends MasterDegreeProofVersion_Base {

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
        this.setMasterDegreeThesis(masterDegreeThesis);
        this.setResponsibleEmployee(responsibleEmployee);
        this.setLastModification(lastModification);
        this.setProofDate(proofDate);
        this.setThesisDeliveryDate(thesisDeliveryDate);
        this.setFinalResult(finalResult);
        this.setAttachedCopiesNumber(attachedCopiesNumber);
        this.setCurrentState(currentState);
        this.setJuries(juries);
        this.setExternalJuries(externalJuries);
    }

    public Timestamp getLastModification() {
        if (this.getLastModificationDate() != null) {
            return new Timestamp(this.getLastModificationDate().getTime());
        }
        return null;
    }

    public void setLastModification(Timestamp lastModification) {
        if (lastModification != null) {
            this.setLastModificationDate(new Date(lastModification.getTime()));
        } else {
            this.setLastModificationDate(null);
        }
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + this.getIdInternal() + "; \n";
        result += "masterDegreeThesis = " + this.getMasterDegreeThesis().getIdInternal() + "; \n";
        result += "responsibleEmployee = " + this.getResponsibleEmployee().getIdInternal() + "; \n";
        result += "lastModification = " + this.getLastModification().toString() + "; \n";
        result += "proofDate = " + this.getProofDate().toString() + "; \n";
        result += "thesisDeliveryDate = " + this.getThesisDeliveryDate().toString() + "; \n";
        result += "finalResult = " + this.getFinalResult().toString() + "; \n";
        result += "attachedCopiesNumber = " + this.getAttachedCopiesNumber().toString() + "; \n";
        result += "currentState = " + this.getCurrentState().toString() + "; \n";
        result += "] \n";

        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof IMasterDegreeProofVersion) {
            IMasterDegreeProofVersion masterDegreeProofVersion = (IMasterDegreeProofVersion) obj;
            result = getMasterDegreeThesis().equals(masterDegreeProofVersion.getMasterDegreeThesis())
                    && this.getLastModification().equals(masterDegreeProofVersion.getLastModification());
        }

        return result;
    }

}
