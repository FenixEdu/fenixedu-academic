/*
 * Created on 2004/03/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class DeleteFinalDegreeWorkProposal implements IService {

    public DeleteFinalDegreeWorkProposal() {
        super();
    }

    public void run(Integer finalDegreeWorkProposalOID) throws FenixServiceException {
        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

            IPersistentFinalDegreeWork persistentFinalWork = persistentSupport
                    .getIPersistentFinalDegreeWork();

            persistentFinalWork.deleteByOID(Proposal.class, finalDegreeWorkProposalOID);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}