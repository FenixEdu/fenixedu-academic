/*
 * Created on 9/Out/2003
 *
 *
 */
package Dominio;

import java.util.List;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class MasterDegreeThesis extends DomainObject implements IMasterDegreeThesis {

    //database internal
    private Integer keyStudentCurricularPlan;

    //fields
    private IStudentCurricularPlan studentCurricularPlan;

    private List masterDegreeProofVersions;

    private List masterDegreeThesisDataVersions;

    public MasterDegreeThesis() {

    }

    /**
     * @param studentCurricularPlan
     */
    public MasterDegreeThesis(IStudentCurricularPlan studentCurricularPlan) {
        super();
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public void setMasterDegreeProofVersions(List masterDegreeProofVersions) {
        this.masterDegreeProofVersions = masterDegreeProofVersions;
    }

    public List getMasterDegreeProofVersions() {
        return masterDegreeProofVersions;
    }

    public void setMasterDegreeThesisDataVersions(List masterDegreeThesisDataVersions) {
        this.masterDegreeThesisDataVersions = masterDegreeThesisDataVersions;
    }

    public List getMasterDegreeThesisDataVersions() {
        return masterDegreeThesisDataVersions;
    }

    public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public IStudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    public void setKeyStudentCurricularPlan(Integer keyStudentCurricularPlan) {
        this.keyStudentCurricularPlan = keyStudentCurricularPlan;
    }

    public Integer getKeyStudentCurricularPlan() {
        return keyStudentCurricularPlan;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "studentCurricularPlan = " + this.studentCurricularPlan.getIdInternal() + "; \n";
        result += "masterDegreeProofVersions = " + this.masterDegreeProofVersions.toString() + "; \n";
        result += "masterDegreeThesisDataVersions = " + this.masterDegreeThesisDataVersions.toString()
                + "; \n";
        result += "] \n";

        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof IMasterDegreeThesis) {
            IMasterDegreeThesis masterDegreeThesis = (IMasterDegreeThesis) obj;
            result = this.studentCurricularPlan.equals(masterDegreeThesis.getStudentCurricularPlan());
        }

        return result;
    }

}