/*
 * Created on 2004/04/04
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.finalDegreeWork.IProposal;
import Dominio.finalDegreeWork.Proposal;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.FinalDegreeWorkProposalStatus;

/**
 * @author Luis Cruz
 *  
 */
public class ChangeStatusOfFinalDegreeWorkProposals implements IService {

	public ChangeStatusOfFinalDegreeWorkProposals() {
		super();
	}

	public void run(Integer executionDegreeOID, List selectedProposalOIDs,
			FinalDegreeWorkProposalStatus status) throws FenixServiceException {
		if (executionDegreeOID != null && selectedProposalOIDs != null) {
			try {
				ISuportePersistente persistentSupport = SuportePersistenteOJB
						.getInstance();
				IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
						.getIPersistentFinalDegreeWork();
				for (int i = 0; i < selectedProposalOIDs.size(); i++) {
					IProposal proposal = (IProposal) persistentFinalDegreeWork
							.readByOID(Proposal.class,
									(Integer) selectedProposalOIDs.get(i));
					persistentFinalDegreeWork.simpleLockWrite(proposal);
					proposal.setStatus(status);
				}
			} catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			}
		}
	}

}