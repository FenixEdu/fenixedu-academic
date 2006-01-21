/*
 * Created on 2004/03/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;

/**
 * @author Luis Cruz
 */
public class DeleteFinalDegreeWorkProposal extends Service {

	public void run(Integer finalDegreeWorkProposalOID) throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentFinalDegreeWork persistentFinalWork = persistentSupport
				.getIPersistentFinalDegreeWork();

		persistentFinalWork.deleteByOID(Proposal.class, finalDegreeWorkProposalOID);
	}
}