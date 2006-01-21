/*
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;

/**
 * @author Luis Cruz
 */
public class ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy extends Service {

    public ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy() {
        super();
    }

    public boolean run(Integer groupOID, Integer groupProposalOID, Integer orderOfPreference)
            throws ExcepcaoPersistencia {
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        Group group = (Group) persistentFinalDegreeWork.readByOID(Group.class, groupOID);
        GroupProposal groupProposal = (GroupProposal) persistentFinalDegreeWork.readByOID(
                GroupProposal.class, groupProposalOID);
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