/*
 * Created on 2004/04/21
 */
package ServidorAplicacao.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.finalDegreeWork.Group;
import Dominio.finalDegreeWork.GroupProposal;
import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IGroupProposal;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy implements IService
{

    public ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy()
    {
        super();
    }

    public boolean run(Integer groupOID, Integer groupProposalOID, Integer orderOfPreference)
            throws ExcepcaoPersistencia
    {
        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroup group = (IGroup) persistentFinalDegreeWork.readByOID(Group.class, groupOID);
        IGroupProposal groupProposal = (IGroupProposal) persistentFinalDegreeWork.readByOID(
                GroupProposal.class, groupProposalOID);
        if (group != null && groupProposal != null)
        {
            for (int i = 0; i < group.getGroupProposals().size(); i++)
            {
                IGroupProposal otherGroupProposal = (IGroupProposal) group.getGroupProposals().get(i);
                if (otherGroupProposal != null
                        && !groupProposal.getIdInternal().equals(otherGroupProposal.getIdInternal()))
                {
                    int otherOrderOfPreference = otherGroupProposal.getOrderOfPreference().intValue();
                    if (orderOfPreference.intValue() <= otherOrderOfPreference
                            && groupProposal.getOrderOfPreference().intValue() > otherOrderOfPreference)
                    {
                        persistentFinalDegreeWork.simpleLockWrite(otherGroupProposal);
                        otherGroupProposal.setOrderOfPreference(new Integer(otherOrderOfPreference + 1));
                    }
                    else if (orderOfPreference.intValue() >= otherOrderOfPreference
                            && groupProposal.getOrderOfPreference().intValue() < otherOrderOfPreference)
                    {
                        persistentFinalDegreeWork.simpleLockWrite(otherGroupProposal);
                        otherGroupProposal.setOrderOfPreference(new Integer(otherOrderOfPreference - 1));                        
                    }
                }
            }
            persistentFinalDegreeWork.simpleLockWrite(groupProposal);
            groupProposal.setOrderOfPreference(orderOfPreference);
        }
        return true;
    }

}