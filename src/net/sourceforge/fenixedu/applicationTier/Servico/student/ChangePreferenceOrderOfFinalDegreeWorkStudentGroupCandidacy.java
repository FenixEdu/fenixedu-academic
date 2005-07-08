/*
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy implements IService {

    public ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy() {
        super();
    }

    public boolean run(Integer groupOID, Integer groupProposalOID, Integer orderOfPreference)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroup group = (IGroup) persistentFinalDegreeWork.readByOID(Group.class, groupOID);
        IGroupProposal groupProposal = (IGroupProposal) persistentFinalDegreeWork.readByOID(
                GroupProposal.class, groupProposalOID);
        if (group != null && groupProposal != null) {
            for (int i = 0; i < group.getGroupProposals().size(); i++) {
                IGroupProposal otherGroupProposal = group.getGroupProposals().get(i);
                if (otherGroupProposal != null
                        && !groupProposal.getIdInternal().equals(otherGroupProposal.getIdInternal())) {
                    int otherOrderOfPreference = otherGroupProposal.getOrderOfPreference().intValue();
                    if (orderOfPreference.intValue() <= otherOrderOfPreference
                            && groupProposal.getOrderOfPreference().intValue() > otherOrderOfPreference) {
                        persistentFinalDegreeWork.simpleLockWrite(otherGroupProposal);
                        otherGroupProposal.setOrderOfPreference(new Integer(otherOrderOfPreference + 1));
                    } else if (orderOfPreference.intValue() >= otherOrderOfPreference
                            && groupProposal.getOrderOfPreference().intValue() < otherOrderOfPreference) {
                        persistentFinalDegreeWork.simpleLockWrite(otherGroupProposal);
                        otherGroupProposal.setOrderOfPreference(new Integer(otherOrderOfPreference - 1));
                    }
                }
            }
            persistentFinalDegreeWork.simpleLockWrite(groupProposal);
            groupProposal.setOrderOfPreference(orderOfPreference);
        }
        return true;
    }

}