/*
 * Created on 2004/04/25
 *
 */
package Dominio.finalDegreeWork;

import Dominio.DomainObject;
import Dominio.IStudent;

/**
 * @author Luis Cruz
 *
 */
public class GroupStudent extends DomainObject implements IGroupStudent {

    private Integer keyFinalDegreeDegreeWorkGroup;
    private Integer keyStudent;
    private Integer keyFinalDegreeWorkProposalConfirmation;

    private IGroup finalDegreeDegreeWorkGroup;
    private IStudent student;
    private IProposal finalDegreeWorkProposalConfirmation;

    public GroupStudent() {
		super();
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IGroupStudent) {
			IGroupStudent groupStudent = (IGroupStudent) obj;

			result = getIdInternal() != null && groupStudent != null && getIdInternal().equals(groupStudent.getIdInternal());
		}
		return result;
	}

	public String toString() {
		String result = "[GroupStudent";
		result += ", idInternal=" + getIdInternal();
		result += "]";
		return result;
	}

    /**
     * @return Returns the finalDegreeDegreeWorkGroup.
     */
    public IGroup getFinalDegreeDegreeWorkGroup()
    {
        return finalDegreeDegreeWorkGroup;
    }
    /**
     * @param finalDegreeDegreeWorkGroup The finalDegreeDegreeWorkGroup to set.
     */
    public void setFinalDegreeDegreeWorkGroup(IGroup finalDegreeDegreeWorkGroup)
    {
        this.finalDegreeDegreeWorkGroup = finalDegreeDegreeWorkGroup;
    }
    /**
     * @return Returns the finalDegreeWorkProposalConfirmation.
     */
    public IProposal getFinalDegreeWorkProposalConfirmation()
    {
        return finalDegreeWorkProposalConfirmation;
    }
    /**
     * @param finalDegreeWorkProposalConfirmation The finalDegreeWorkProposalConfirmation to set.
     */
    public void setFinalDegreeWorkProposalConfirmation(IProposal finalDegreeWorkProposalConfirmation)
    {
        this.finalDegreeWorkProposalConfirmation = finalDegreeWorkProposalConfirmation;
    }
    /**
     * @return Returns the keyFinalDegreeDegreeWorkGroup.
     */
    public Integer getKeyFinalDegreeDegreeWorkGroup()
    {
        return keyFinalDegreeDegreeWorkGroup;
    }
    /**
     * @param keyFinalDegreeDegreeWorkGroup The keyFinalDegreeDegreeWorkGroup to set.
     */
    public void setKeyFinalDegreeDegreeWorkGroup(Integer keyFinalDegreeDegreeWorkGroup)
    {
        this.keyFinalDegreeDegreeWorkGroup = keyFinalDegreeDegreeWorkGroup;
    }
    /**
     * @return Returns the keyFinalDegreeWorkProposalConfirmation.
     */
    public Integer getKeyFinalDegreeWorkProposalConfirmation()
    {
        return keyFinalDegreeWorkProposalConfirmation;
    }
    /**
     * @param keyFinalDegreeWorkProposalConfirmation The keyFinalDegreeWorkProposalConfirmation to set.
     */
    public void setKeyFinalDegreeWorkProposalConfirmation(Integer keyFinalDegreeWorkProposalConfirmation)
    {
        this.keyFinalDegreeWorkProposalConfirmation = keyFinalDegreeWorkProposalConfirmation;
    }
    /**
     * @return Returns the keyStudent.
     */
    public Integer getKeyStudent()
    {
        return keyStudent;
    }
    /**
     * @param keyStudent The keyStudent to set.
     */
    public void setKeyStudent(Integer keyStudent)
    {
        this.keyStudent = keyStudent;
    }
    /**
     * @return Returns the student.
     */
    public IStudent getStudent()
    {
        return student;
    }
    /**
     * @param student The student to set.
     */
    public void setStudent(IStudent student)
    {
        this.student = student;
    }
}