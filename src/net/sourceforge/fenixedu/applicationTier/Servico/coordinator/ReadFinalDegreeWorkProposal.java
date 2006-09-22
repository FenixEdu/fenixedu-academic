/*
 * Created on 2004/03/09
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposal extends Service {

    public InfoProposal run(Integer finalDegreeWorkProposalOID) throws FenixServiceException, ExcepcaoPersistencia {
	return InfoProposal.newInfoFromDomain(rootDomainObject.readProposalByOID(finalDegreeWorkProposalOID));
    }
    
}
