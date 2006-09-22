/*
 * Created on 2004/04/15
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;

/**
 * @author Luis Cruz
 *  
 */
public class InfoGroupStudent extends InfoObject {

    private DomainReference<GroupStudent> groupStudentDomainReference;
    
    public InfoGroupStudent(final GroupStudent groupStudent) {
	groupStudentDomainReference = new DomainReference<GroupStudent>(groupStudent);
    }
    
    public static InfoGroupStudent newInfoFromDomain(GroupStudent groupStudent) {
	return groupStudent == null ? null : new InfoGroupStudent(groupStudent);
    }
    
    private GroupStudent getGroupStudent() {
	return groupStudentDomainReference == null ?  null : groupStudentDomainReference.getObject();
    }
    
    public boolean equals(Object obj) {
	return obj instanceof InfoGroupStudent
		&& getGroupStudent() == ((InfoGroupStudent) obj).getGroupStudent();
    }

    public int hashCode() {
	return getGroupStudent().hashCode();
    }

    @Override
    public Integer getIdInternal() {
	return getGroupStudent().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

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
	return InfoStudent.newInfoFromDomain(getGroupStudent().getStudent());
    }

}
