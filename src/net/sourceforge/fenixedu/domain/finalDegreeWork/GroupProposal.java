/*
 * Created on 2004/04/19
 *
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

/**
 * @author Luis Cruz
 *  
 */
public class GroupProposal extends GroupProposal_Base {

    public GroupProposal() {
        super();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IGroupProposal) {
            IGroupProposal groupProposal = (IGroupProposal) obj;

            result = getIdInternal() != null && groupProposal != null
                    && getIdInternal().equals(groupProposal.getIdInternal());
        }
        return result;
    }

    public String toString() {
        String result = "[ProposalGroup";
        result += ", idInternal=" + getIdInternal();
        result += "]";
        return result;
    }
}