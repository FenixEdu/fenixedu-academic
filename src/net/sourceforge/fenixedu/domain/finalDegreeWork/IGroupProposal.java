/*
 * Created on Apr 15, 2004
 *
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author Luis Cruz
 *  
 */
public interface IGroupProposal extends IDomainObject {

    public IGroup getFinalDegreeDegreeWorkGroup();

    public void setFinalDegreeDegreeWorkGroup(IGroup finalDegreeDegreeWorkGroup);

    public IProposal getFinalDegreeWorkProposal();

    public void setFinalDegreeWorkProposal(IProposal finalDegreeWorkProposal);

    public Integer getOrderOfPreference();

    public void setOrderOfPreference(Integer orderOfPreference);

}