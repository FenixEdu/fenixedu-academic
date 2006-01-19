package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import net.sourceforge.fenixedu.applicationTier.Service;

public class PublishAprovedFinalDegreeWorkProposals extends Service {

    public void run(Integer executionDegreeOID) throws FenixServiceException, ExcepcaoPersistencia {
        if (executionDegreeOID != null) {

            ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();

            List<Proposal> aprovedFinalDegreeWorkProposals = persistentFinalDegreeWork
                    .readAprovedFinalDegreeWorkProposals(executionDegreeOID);

            if (aprovedFinalDegreeWorkProposals != null && !aprovedFinalDegreeWorkProposals.isEmpty()) {
                for (Proposal proposal : aprovedFinalDegreeWorkProposals) {
                    proposal.setStatus(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
                }
            }
        }
    }

}
