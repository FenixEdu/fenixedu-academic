/*
 * Created on 2004/04/19
 *  
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader;
import Dominio.finalDegreeWork.Group;
import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IGroupProposal;
import Dominio.finalDegreeWork.IProposal;
import Dominio.finalDegreeWork.Proposal;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class ReadAvailableFinalDegreeWorkProposalHeadersForGroup implements IService
{

    public ReadAvailableFinalDegreeWorkProposalHeadersForGroup()
    {
        super();
    }

    public List run(Integer groupOID) throws FenixServiceException
    {
        List finalDegreeWorkProposalHeaders = new ArrayList();

        try
        {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport.getIPersistentFinalDegreeWork();

            IGroup group = (IGroup) persistentFinalDegreeWork.readByOID(Group.class, groupOID);

            if (group != null && group.getExecutionDegree() != null)
            {
                List finalDegreeWorkProposals = persistentFinalDegreeWork
                        .readPublishedFinalDegreeWorkProposalsByExecutionDegree(group.getExecutionDegree()
                                .getIdInternal());

                if (finalDegreeWorkProposals != null)
                {
                    finalDegreeWorkProposalHeaders = new ArrayList();
                    for (int i = 0; i < finalDegreeWorkProposals.size(); i++)
                    {
                        IProposal proposal = (Proposal) finalDegreeWorkProposals.get(i);

                        if (proposal != null
                                && !CollectionUtils.exists(group.getGroupProposals(),
                                        new PREDICATE_FIND_GROUP_PROPOSAL_BY_PROPOSAL(proposal)))
                        {
                            FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader = new FinalDegreeWorkProposalHeader();

                            finalDegreeWorkProposalHeader.setIdInternal(proposal.getIdInternal());
                            finalDegreeWorkProposalHeader.setProposalNumber(proposal.getProposalNumber());
                            finalDegreeWorkProposalHeader.setTitle(proposal.getTitle());
                            if (proposal.getOrientator() != null)
                            {
                                finalDegreeWorkProposalHeader
                                        .setOrientatorOID(proposal.getOrientator().getIdInternal());
                                finalDegreeWorkProposalHeader.setOrientatorName(proposal.getOrientator().getPerson()
                                        .getNome());
                            }
                            if (proposal.getCoorientator() != null)
                            {
                                finalDegreeWorkProposalHeader.setCoorientatorOID(proposal.getCoorientator()
                                        .getIdInternal());
                                finalDegreeWorkProposalHeader.setCoorientatorName(proposal.getCoorientator()
                                        .getPerson().getNome());
                            }
                            finalDegreeWorkProposalHeader.setCompanyLink(proposal.getCompanionName());
                            finalDegreeWorkProposalHeader.setStatus(proposal.getStatus());

                            finalDegreeWorkProposalHeaders.add(finalDegreeWorkProposalHeader);
                        }
                    }
                }
            }
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

        return finalDegreeWorkProposalHeaders;
    }

    private class PREDICATE_FIND_GROUP_PROPOSAL_BY_PROPOSAL implements Predicate
    {

        IProposal proposal;

        public boolean evaluate(Object arg0)
        {
            IGroupProposal groupProposal = (IGroupProposal) arg0;
            return proposal.equals(groupProposal.getFinalDegreeWorkProposal());
        }

        public PREDICATE_FIND_GROUP_PROPOSAL_BY_PROPOSAL(IProposal proposal)
        {
            super();
            this.proposal = proposal;
        }
    };

}