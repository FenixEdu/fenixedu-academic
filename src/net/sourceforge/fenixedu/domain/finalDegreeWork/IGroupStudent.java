/*
 * Created on 2004/04/25
 *
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IStudent;

/**
 * @author Luis Cruz
 *  
 */
public interface IGroupStudent extends IDomainObject {

    public IGroup getFinalDegreeDegreeWorkGroup();

    public void setFinalDegreeDegreeWorkGroup(IGroup finalDegreeDegreeWorkGroup);

    public IProposal getFinalDegreeWorkProposalConfirmation();

    public void setFinalDegreeWorkProposalConfirmation(IProposal finalDegreeWorkProposalConfirmation);

    public Integer getKeyFinalDegreeDegreeWorkGroup();

    public void setKeyFinalDegreeDegreeWorkGroup(Integer keyFinalDegreeDegreeWorkGroup);

    public Integer getKeyFinalDegreeWorkProposalConfirmation();

    public void setKeyFinalDegreeWorkProposalConfirmation(Integer keyFinalDegreeWorkProposalConfirmation);

    public Integer getKeyStudent();

    public void setKeyStudent(Integer keyStudent);

    public IStudent getStudent();

    public void setStudent(IStudent student);

}