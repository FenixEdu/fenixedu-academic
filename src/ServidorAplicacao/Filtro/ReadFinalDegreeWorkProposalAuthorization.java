/*
 * Created on 2004/04/06
 * 
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import Dominio.finalDegreeWork.IProposal;
import Dominio.finalDegreeWork.Proposal;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.FinalDegreeWorkProposalStatus;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposalAuthorization extends Filtro
{
    public ReadFinalDegreeWorkProposalAuthorization()
    {
    	super();
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception
    {
        IUserView id = getRemoteUser(request);
        Integer finalDegreeWorkProposalOID = (Integer) request.getArguments()[0];
        if (finalDegreeWorkProposalOID != null)
        {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentFinalDegreeWork persistentFinalDegreeWork =
				persistentSupport.getIPersistentFinalDegreeWork();

			IProposal proposal =
				(IProposal) persistentFinalDegreeWork.readByOID(
					Proposal.class,
					finalDegreeWorkProposalOID);
			if (proposal != null && proposal.getStatus() != null && proposal.getStatus().equals(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS))
			{
				return;
			}
        }
        throw new NotAuthorizedFilterException();
    }

}