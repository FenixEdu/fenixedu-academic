/*
 * Created on 2004/04/04
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.finalDegreeWork.IProposal;
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
public class PublishAprovedFinalDegreeWorkProposals implements IService {

	public PublishAprovedFinalDegreeWorkProposals() {
		super();
	}

	public void run(Integer executionDegreeOID) throws FenixServiceException {
		if (executionDegreeOID != null) {
			try {
				ISuportePersistente persistentSupport = SuportePersistenteOJB
						.getInstance();
				IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
						.getIPersistentFinalDegreeWork();

				List aprovedFinalDegreeWorkProposals = persistentFinalDegreeWork
						.readAprovedFinalDegreeWorkProposals(executionDegreeOID);
				if (aprovedFinalDegreeWorkProposals != null && !aprovedFinalDegreeWorkProposals.isEmpty())
				{
					for (int i = 0; i < aprovedFinalDegreeWorkProposals.size(); i++)
					{
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