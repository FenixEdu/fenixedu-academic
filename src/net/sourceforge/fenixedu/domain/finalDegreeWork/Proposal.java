/*
 * Created on Mar 7, 2004
 *  
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;


/**
 * @author Luis Cruz
 *  
 */
public class Proposal extends Proposal_Base {

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IProposal) {
            IProposal proposal = (IProposal) obj;

            result = getIdInternal().equals(proposal.getIdInternal());
        }
        return result;
    }

    public String toString() {
        String result = "[Proposal";
        result += ", idInternal=" + getIdInternal();
        result += ", title=" + getTitle();
        result += ", degreeCurricularPlan=" + getExecutionDegree();
        result += "]";
        return result;
    }

}