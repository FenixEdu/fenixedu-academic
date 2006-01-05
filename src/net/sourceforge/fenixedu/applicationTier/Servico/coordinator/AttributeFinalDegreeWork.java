package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AttributeFinalDegreeWork implements IService {

    public void run(Integer selectedGroupProposal) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroupProposal groupProposal = (IGroupProposal) persistentFinalDegreeWork.readByOID(
                GroupProposal.class, selectedGroupProposal);
        if (groupProposal != null) {
            IProposal proposal = groupProposal.getFinalDegreeWorkProposal();
            proposal.setGroupAttributed(groupProposal.getFinalDegreeDegreeWorkGroup());

            IGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
            for (IGroupProposal otherGroupProposal : group.getGroupProposals()) {
                if (!(otherGroupProposal == groupProposal)
                        && otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed() != null
                        && (otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed() == group)) {
                    IProposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
                    otherProposal.removeGroupAttributed();
                }
            }
        }
    }

}