/*
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy extends Service {

    public ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy() {
        super();
    }

    public boolean run(Integer groupOID, Integer groupProposalOID, Integer orderOfPreference)
            throws ExcepcaoPersistencia {
        FinalDegreeWorkGroup group = rootDomainObject.readFinalDegreeWorkGroupByOID(groupOID);
        GroupProposal groupProposal = rootDomainObject.readGroupProposalByOID(groupProposalOID);
        if (group != null && groupProposal != null) {
            for (int i = 0; i < group.getGroupProposals().size(); i++) {
                GroupProposal otherGroupProposal = group.getGroupProposals().get(i);
                if (otherGroupProposal != null
                        && !groupProposal.getIdInternal().equals(otherGroupProposal.getIdInternal())) {
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