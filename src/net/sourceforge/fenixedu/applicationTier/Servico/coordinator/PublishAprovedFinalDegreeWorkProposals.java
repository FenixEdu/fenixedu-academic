/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 *  
 */
public class PublishAprovedFinalDegreeWorkProposals implements IService {

    public PublishAprovedFinalDegreeWorkProposals() {
        super();
    }

    public void run(Integer executionDegreeOID) throws FenixServiceException {
        if (executionDegreeOID != null) {
            try {
                ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
                IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                        .getIPersistentFinalDegreeWork();

                List aprovedFinalDegreeWorkProposals = persistentFinalDegreeWork
                        .readAprovedFinalDegreeWorkProposals(executionDegreeOID);
                if (aprovedFinalDegreeWorkProposals != null
                        && !aprovedFinalDegreeWorkProposals.isEmpty()) {
                    for (int i = 0; i < aprovedFinalDegreeWorkProposals.size(); i++) {
                        IProposal proposal = (IProposal) aprovedFinalDegreeWorkProposals.get(i);
                        persistentFinalDegreeWork.simpleLockWrite(proposal);
                        proposal.setStatus(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
                    }
                }
            } catch (ExcepcaoPersistencia e) {
                throw new FenixServiceException(e);
            }
        }
    }

}