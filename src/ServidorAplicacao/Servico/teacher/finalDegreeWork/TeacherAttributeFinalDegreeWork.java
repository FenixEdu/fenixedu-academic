/*
 * Created on 2004/04/24
 *
 */
package ServidorAplicacao.Servico.teacher.finalDegreeWork;

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
public class TeacherAttributeFinalDegreeWork implements IService
{

	public TeacherAttributeFinalDegreeWork()
	{
		super();
	}

	public Boolean run(Integer selectedGroupProposalOID) throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentFinalDegreeWork persistentFinalWork = persistentSupport
					.getIPersistentFinalDegreeWork();

			IGroupProposal groupProposal = (IGroupProposal) persistentFinalWork.readByOID(GroupProposal.class, selectedGroupProposalOID);
			if (groupProposal != null)
			{
			    IProposal proposal = groupProposal.getFinalDegreeWorkProposal();
			    IGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
			    if (proposal != null && group != null)
			    {
			        persistentFinalWork.simpleLockWrite(proposal);
			        if (proposal.getGroupAttributedByTeacher() == null || !proposal.getGroupAttributedByTeacher().equals(group))
			        {
			            proposal.setGroupAttributedByTeacher(group);
			            for (int i = 0; i < group.getGroupProposals().size(); i++)
			            {
			                IGroupProposal otherGroupProposal = (IGroupProposal) group.getGroupProposals().get(i);
			                IProposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
			                if (!otherProposal.getIdInternal().equals(proposal.getIdInternal())
			                        && group.equals(otherProposal.getGroupAttributedByTeacher()))
			                {
			                    persistentFinalWork.simpleLockWrite(otherProposal);
			                    otherProposal.setGroupAttributedByTeacher(null);
			                }
			            }
			        }
			        else
			        {
			            proposal.setGroupAttributedByTeacher(null);
			        }
			    }
			}
			return new Boolean(true);
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}

}