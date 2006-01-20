package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

public class ChangeStatusOfFinalDegreeWorkProposals extends Service {

    public void run(Integer executionDegreeOID, List<Integer> selectedProposalOIDs,
            FinalDegreeWorkProposalStatus status) throws FenixServiceException, ExcepcaoPersistencia {
        if (executionDegreeOID != null && selectedProposalOIDs != null) {

            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();
            for (Integer selectedProposalOID : selectedProposalOIDs) {
                Proposal proposal = (Proposal) persistentFinalDegreeWork.readByOID(Proposal.class,
                        selectedProposalOID);
                proposal.setStatus(status);
            }

        }
    }

}
