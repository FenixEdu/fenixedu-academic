/*
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 */
public class ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy {

    public ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy() {
        super();
    }

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static Boolean run(FinalDegreeWorkGroup group, Integer groupProposalOID, Integer orderOfPreference) {
        GroupProposal groupProposal = RootDomainObject.getInstance().readGroupProposalByOID(groupProposalOID);
        if (group != null && groupProposal != null) {
            for (int i = 0; i < group.getGroupProposals().size(); i++) {
                GroupProposal otherGroupProposal = group.getGroupProposals().get(i);
                if (otherGroupProposal != null && !groupProposal.getExternalId().equals(otherGroupProposal.getExternalId())) {
                    int otherOrderOfPreference = otherGroupProposal.getOrderOfPreference().intValue();
                    if (orderOfPreference.intValue() <= otherOrderOfPreference
                            && groupProposal.getOrderOfPreference().intValue() > otherOrderOfPreference) {
                        otherGroupProposal.setOrderOfPreference(new Integer(otherOrderOfPreference + 1));
                    } else if (orderOfPreference.intValue() >= otherOrderOfPreference
                            && groupProposal.getOrderOfPreference().intValue() < otherOrderOfPreference) {
                        otherGroupProposal.setOrderOfPreference(new Integer(otherOrderOfPreference - 1));
                    }
                }
            }
            groupProposal.setOrderOfPreference(orderOfPreference);
        }
        return true;
    }

}