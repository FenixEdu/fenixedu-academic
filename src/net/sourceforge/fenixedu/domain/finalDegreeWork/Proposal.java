/*
 * Created on Mar 7, 2004
 *  
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

/**
 * @author Luis Cruz
 *  
 */
public class Proposal extends Proposal_Base {
    private DegreeType degreeType; 
    private FinalDegreeWorkProposalStatus status;

    public Proposal() {
        super();
    }

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

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public FinalDegreeWorkProposalStatus getStatus() {
        return status;
    }

    public void setStatus(FinalDegreeWorkProposalStatus status) {
        this.status = status;
    }
}