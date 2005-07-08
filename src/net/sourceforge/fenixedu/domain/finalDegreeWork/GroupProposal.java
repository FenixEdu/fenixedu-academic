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

    public String toString() {
        String result = "[ProposalGroup";
        result += ", idInternal=" + getIdInternal();
        result += "]";
        return result;
    }
    
}