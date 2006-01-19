package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class AttributeFinalDegreeWork extends Service {

    public void run(Integer selectedGroupProposal) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        GroupProposal groupProposal = (GroupProposal) persistentFinalDegreeWork.readByOID(
                GroupProposal.class, selectedGroupProposal);
        if (groupProposal != null) {
            Proposal proposal = groupProposal.getFinalDegreeWorkProposal();
            proposal.setGroupAttributed(groupProposal.getFinalDegreeDegreeWorkGroup());

            Group group = groupProposal.getFinalDegreeDegreeWorkGroup();
            for (GroupProposal otherGroupProposal : group.getGroupProposals()) {
                if (!(otherGroupProposal == groupProposal)
                        && otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed() != null
                        && (otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed() == group)) {
                    Proposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
                    otherProposal.removeGroupAttributed();
                }
            }
        }
    }

}