/*
 * Created on 2004/03/14
 *
 */
package ServidorAplicacao.Servico.teacher.finalDegreeWork;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.finalDegreeWork.Proposal;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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