/*
 * Created on 2004/04/25
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class AttributeFinalDegreeWork implements IService {

    public AttributeFinalDegreeWork() {
        super();
    }

    public void run(Integer selectedGroupProposal) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroupProposal groupProposal = (IGroupProposal) persistentFinalDegreeWork.readByOID(
                GroupProposal.class, selectedGroupProposal);
        if (groupProposal != null) {
            IProposal proposal = groupProposal.getFinalDegreeWorkProposal();
            persistentFinalDegreeWork.simpleLockWrite(proposal);
            proposal.setGroupAttributed(groupProposal.getFinalDegreeDegreeWorkGroup());

            IGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
            for (int i = 0; i < group.getGroupProposals().size(); i++) {
                IGroupProposal otherGroupProposal = (IGroupProposal) group.getGroupProposals().get(i);
                if (!otherGroupProposal.getIdInternal().equals(groupProposal.getIdInternal())
                        && otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed() != null
                        && otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed()
                                .getIdInternal().equals(group.getIdInternal())) {
                    IProposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
                    persistentFinalDegreeWork.simpleLockWrite(otherProposal);
                    otherProposal.setGroupAttributed(null);
                }
            }
        }
    }

}