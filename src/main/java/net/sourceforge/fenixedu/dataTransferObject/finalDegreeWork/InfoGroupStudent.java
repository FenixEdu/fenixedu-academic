/*
 * Created on 2004/04/15
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;

/**
 * @author Luis Cruz
 * 
 */
public class InfoGroupStudent extends InfoObject {

    private GroupStudent groupStudentDomainReference;

    public InfoGroupStudent(final GroupStudent groupStudent) {
        groupStudentDomainReference = groupStudent;
    }

    public static InfoGroupStudent newInfoFromDomain(GroupStudent groupStudent) {
        return groupStudent == null ? null : new InfoGroupStudent(groupStudent);
    }

    private GroupStudent getGroupStudent() {
        return groupStudentDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoGroupStudent && getGroupStudent() == ((InfoGroupStudent) obj).getGroupStudent();
    }

    @Override
    public int hashCode() {
        return getGroupStudent().hashCode();
    }

    @Override
    public Integer getExternalId() {
        return getGroupStudent().getExternalId();
    }

    @Override
    public void setExternalId(Integer integer) {
        throw new Error("Method should not be called!");
    }

    @Override
    public String toString() {
        return getGroupStudent().toString();
    }

    /**
     * @return Returns the finalDegreeDegreeWorkGroup.
     */
    public InfoGroup getFinalDegreeDegreeWorkGroup() {
        return InfoGroup.newInfoFromDomain(getGroupStudent().getFinalDegreeDegreeWorkGroup());
    }

    /**
     * @return Returns the finalDegreeWorkProposalConfirmation.
     */
    public InfoProposal getFinalDegreeWorkProposalConfirmation() {
        return InfoProposal.newInfoFromDomain(getGroupStudent().getFinalDegreeWorkProposalConfirmation());
    }

    /**
     * @return Returns the student.
     */
    public InfoStudent getStudent() {
        return InfoStudent.newInfoFromDomain(getGroupStudent().getRegistration());
    }

}
