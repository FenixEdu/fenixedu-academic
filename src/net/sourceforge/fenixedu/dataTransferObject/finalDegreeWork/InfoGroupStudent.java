/*
 * Created on 2004/04/15
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;

/**
 * @author Luis Cruz
 *  
 */
public class InfoGroupStudent extends InfoObject {

    private InfoGroup finalDegreeDegreeWorkGroup;

    private InfoStudent student;

    private InfoProposal finalDegreeWorkProposalConfirmation;

    public InfoGroupStudent() {
        super();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoGroupStudent) {
            InfoGroupStudent group = (InfoGroupStudent) obj;

            if (group.getIdInternal() != null && getIdInternal() != null) {
                result = group.getIdInternal().equals(getIdInternal());
            }
        }
        return result;
    }

    public String toString() {
        String result = "[InfoGroupStudent";
        result += ", idInternal=" + getIdInternal();
        result += "]";
        return result;
    }

    /**
     * @return Returns the finalDegreeDegreeWorkGroup.
     */
    public InfoGroup getFinalDegreeDegreeWorkGroup() {
        return finalDegreeDegreeWorkGroup;
    }

    /**
     * @param finalDegreeDegreeWorkGroup
     *            The finalDegreeDegreeWorkGroup to set.
     */
    public void setFinalDegreeDegreeWorkGroup(InfoGroup finalDegreeDegreeWorkGroup) {
        this.finalDegreeDegreeWorkGroup = finalDegreeDegreeWorkGroup;
    }

    /**
     * @return Returns the finalDegreeWorkProposalConfirmation.
     */
    public InfoProposal getFinalDegreeWorkProposalConfirmation() {
        return finalDegreeWorkProposalConfirmation;
    }

    /**
     * @param finalDegreeWorkProposalConfirmation
     *            The finalDegreeWorkProposalConfirmation to set.
     */
    public void setFinalDegreeWorkProposalConfirmation(InfoProposal finalDegreeWorkProposalConfirmation) {
        this.finalDegreeWorkProposalConfirmation = finalDegreeWorkProposalConfirmation;
    }

    /**
     * @return Returns the student.
     */
    public InfoStudent getStudent() {
        return student;
    }

    /**
     * @param student
     *            The student to set.
     */
    public void setStudent(InfoStudent student) {
        this.student = student;
    }
}