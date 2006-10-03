/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class InfoMasterDegreeProofVersion extends InfoObject {

    private InfoMasterDegreeThesis infoMasterDegreeThesis;

    private InfoEmployee infoResponsibleEmployee;

    private Timestamp lastModification;

    private Date proofDate;

    private Date thesisDeliveryDate;

    private MasterDegreeClassification finalResult;

    private Integer attachedCopiesNumber;

    private State currentState;

    private List infoJuries; /* Teachers */

    private List infoExternalJuries; /* External Persons */

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "infoMasterDegreeThesis = " + this.infoMasterDegreeThesis.getIdInternal() + "; \n";
        result += "infoResponsibleEmployee = " + this.infoResponsibleEmployee.getIdInternal() + "; \n";
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

        if (obj instanceof InfoMasterDegreeProofVersion) {
            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) obj;
            result = this.getInfoMasterDegreeThesis().equals(
                    infoMasterDegreeProofVersion.getInfoMasterDegreeThesis())
                    && this.getLastModification().equals(
                            infoMasterDegreeProofVersion.getLastModification());
        }
        return result;
    }

    public void setAttachedCopiesNumber(Integer attachedCopiesNumber) {
        this.attachedCopiesNumber = attachedCopiesNumber;
    }

    public Integer getAttachedCopiesNumber() {
        return attachedCopiesNumber;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setFinalResult(MasterDegreeClassification finalResult) {
        this.finalResult = finalResult;
    }

    public MasterDegreeClassification getFinalResult() {
        return finalResult;
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

    public void setInfoJuries(List infoJuries) {
        this.infoJuries = infoJuries;
    }

    public List getInfoJuries() {
        return infoJuries;
    }

    public List getInfoExternalJuries() {
        return infoExternalJuries;
    }

    public void setInfoExternalJuries(List infoExternalJuries) {
        this.infoExternalJuries = infoExternalJuries;
    }

    /**
     * @param externalPersons
     * @return
     */
    private List copyExternalPersons(List externalPersons) {
        return (List) CollectionUtils.collect(externalPersons, new Transformer() {
            public Object transform(Object arg0) {
        	ExternalContract externalPerson = (ExternalContract) arg0;
                return InfoExternalPerson.newInfoFromDomain(externalPerson);
            }
        });
    }

    /**
     * @param masterDegreeThesisDataVersion
     * @return
     */
    private List copyTeachers(List teachers) {
        return (List) CollectionUtils.collect(teachers, new Transformer() {
            public Object transform(Object arg0) {
                Teacher teacher = (Teacher) arg0;
                return InfoTeacher.newInfoFromDomain(teacher);
            }
        });
    }

    public static InfoMasterDegreeProofVersion newInfoFromDomain(
            MasterDegreeProofVersion masterDegreeProofVersion) {
        InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;
        if (masterDegreeProofVersion != null) {
            infoMasterDegreeProofVersion = new InfoMasterDegreeProofVersion();
            infoMasterDegreeProofVersion.copyFromDomain(masterDegreeProofVersion);
        }
        return infoMasterDegreeProofVersion;
    }

    protected void copyFromDomain(MasterDegreeProofVersion masterDegreeProofVersion) {
        super.copyFromDomain(masterDegreeProofVersion);
        if (masterDegreeProofVersion != null) {
            this.setInfoMasterDegreeThesis(InfoMasterDegreeThesis
                    .newInfoFromDomain(masterDegreeProofVersion.getMasterDegreeThesis()));
            this.setAttachedCopiesNumber(masterDegreeProofVersion.getAttachedCopiesNumber());
            this.setCurrentState(masterDegreeProofVersion.getCurrentState());
            this.setFinalResult(masterDegreeProofVersion.getFinalResult());
            if (masterDegreeProofVersion.getLastModification() != null) {
                this.setLastModification(new Timestamp(masterDegreeProofVersion.getLastModification()
                        .getTime()));
            }
            this.setProofDate(masterDegreeProofVersion.getProofDate());
            this.setThesisDeliveryDate(masterDegreeProofVersion.getThesisDeliveryDate());
            this.setInfoJuries(copyTeachers(masterDegreeProofVersion.getJuries()));
            this
                    .setInfoExternalJuries(copyExternalPersons(masterDegreeProofVersion
                            .getExternalJuries()));
            this.setInfoResponsibleEmployee(InfoEmployee.newInfoFromDomain(masterDegreeProofVersion.getResponsibleEmployee()));
        }

    }

}