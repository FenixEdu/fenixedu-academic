/*
 * Created on 2004/04/19
 *
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author Luis Cruz
 *  
 */
public class GroupProposal extends DomainObject implements IGroupProposal {

    private Integer keyFinalDegreeDegreeWorkGroup;

    private Integer keyFinalDegreeWorkProposal;

    private Integer orderOfPreference;

    private IGroup finalDegreeDegreeWorkGroup;

    private IProposal finalDegreeWorkProposal;

    public GroupProposal() {
        super();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IGroupProposal) {
            IGroupProposal groupProposal = (IGroupProposal) obj;

            result = getIdInternal() != null && groupProposal != null
                    && getIdInternal().equals(groupProposal.getIdInternal());
            /*
             * && keyFinalDegreeDegreeWorkGroup != null &&
             * groupProposal.getFinalDegreeDegreeWorkGroup() != null &&
             * keyFinalDegreeDegreeWorkGroup.equals(groupProposal.getFinalDegreeDegreeWorkGroup().getIdInternal()) &&
             * keyFinalDegreeWorkProposal != null &&
             * groupProposal.getFinalDegreeWorkProposal() != null &&
             * keyFinalDegreeWorkProposal.equals(groupProposal.getFinalDegreeWorkProposal().getIdInternal());
             */
        }
        return result;
    }

    public String toString() {
        String result = "[ProposalGroup";
        result += ", idInternal=" + getIdInternal();
        result += "]";
        return result;
    }

    /**
     * @return Returns the keyFinalDegreeDegreeWorkGroup.
     */
    public Integer getKeyFinalDegreeDegreeWorkGroup() {
        return keyFinalDegreeDegreeWorkGroup;
    }

    /**
     * @param keyFinalDegreeDegreeWorkGroup
     *            The keyFinalDegreeDegreeWorkGroup to set.
     */
    public void setKeyFinalDegreeDegreeWorkGroup(Integer keyFinalDegreeDegreeWorkGroup) {
        this.keyFinalDegreeDegreeWorkGroup = keyFinalDegreeDegreeWorkGroup;
    }

    /**
     * @return Returns the keyFinalDegreeWorkProposal.
     */
    public Integer getKeyFinalDegreeWorkProposal() {
        return keyFinalDegreeWorkProposal;
    }

    /**
     * @param keyFinalDegreeWorkProposal
     *            The keyFinalDegreeWorkProposal to set.
     */
    public void setKeyFinalDegreeWorkProposal(Integer keyFinalDegreeWorkProposal) {
        this.keyFinalDegreeWorkProposal = keyFinalDegreeWorkProposal;
    }

    /**
     * @return Returns the finalDegreeDegreeWorkGroup.
     */
    public IGroup getFinalDegreeDegreeWorkGroup() {
        return finalDegreeDegreeWorkGroup;
    }

    /**
     * @param finalDegreeDegreeWorkGroup
     *            The finalDegreeDegreeWorkGroup to set.
     */
    public void setFinalDegreeDegreeWorkGroup(IGroup finalDegreeDegreeWorkGroup) {
        this.finalDegreeDegreeWorkGroup = finalDegreeDegreeWorkGroup;
    }

    /**
     * @return Returns the finalDegreeWorkProposal.
     */
    public IProposal getFinalDegreeWorkProposal() {
        return finalDegreeWorkProposal;
    }

    /**
     * @param finalDegreeWorkProposal
     *            The finalDegreeWorkProposal to set.
     */
    public void setFinalDegreeWorkProposal(IProposal finalDegreeWorkProposal) {
        this.finalDegreeWorkProposal = finalDegreeWorkProposal;
    }

    /**
     * @return Returns the orderOfPreference.
     */
    public Integer getOrderOfPreference() {
        return orderOfPreference;
    }

    /**
     * @param orderOfPreference
     *            The orderOfPreference to set.
     */
    public void setOrderOfPreference(Integer orderOfPreference) {
        this.orderOfPreference = orderOfPreference;
    }

}