/*
 * Created on 2004/04/25
 */
package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.finalDegreeWork.GroupProposal;
import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IGroupProposal;
import Dominio.finalDegreeWork.IProposal;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class AttributeFinalDegreeWork implements IService
{

    public AttributeFinalDegreeWork()
    {
        super();
    }

    public void run(Integer selectedGroupProposal) throws FenixServiceException, ExcepcaoPersistencia
    {
        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroupProposal groupProposal = (IGroupProposal) persistentFinalDegreeWork.readByOID(
                GroupProposal.class, selectedGroupProposal);
        if (groupProposal != null)
        {
            IProposal proposal = groupProposal.getFinalDegreeWorkProposal();
            persistentFinalDegreeWork.simpleLockWrite(proposal);
            proposal.setGroupAttributed(groupProposal.getFinalDegreeDegreeWorkGroup());

            IGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
            for (int i = 0; i < group.getGroupProposals().size(); i++)
            {
                IGroupProposal otherGroupProposal = (IGroupProposal) group.getGroupProposals().get(i);
                if (!otherGroupProposal.getIdInternal().equals(groupProposal.getIdInternal())
                        && otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed() != null
                        && otherGroupProposal.getFinalDegreeWorkProposal().getGroupAttributed().getIdInternal().equals(group.getIdInternal()))
                {
                    IProposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
                    persistentFinalDegreeWork.simpleLockWrite(otherProposal);
                    otherProposal.setGroupAttributed(null);
                }
            }
        }
    }

}