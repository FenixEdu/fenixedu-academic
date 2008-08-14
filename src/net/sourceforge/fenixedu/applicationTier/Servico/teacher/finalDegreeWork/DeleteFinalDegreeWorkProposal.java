/*
 * Created on 2004/03/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class DeleteFinalDegreeWorkProposal extends Service {

    public void run(Integer finalDegreeWorkProposalOID) throws FenixServiceException {
	rootDomainObject.readProposalByOID(finalDegreeWorkProposalOID).delete();
    }

}
