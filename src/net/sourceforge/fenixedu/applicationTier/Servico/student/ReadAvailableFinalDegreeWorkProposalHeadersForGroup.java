/*
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class ReadAvailableFinalDegreeWorkProposalHeadersForGroup implements IService {

    public ReadAvailableFinalDegreeWorkProposalHeadersForGroup() {
        super();
    }

    public List run(Integer groupOID) throws FenixServiceException {
        List finalDegreeWorkProposalHeaders = new ArrayList();

        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();

            IGroup group = (IGroup) persistentFinalDegreeWork.readByOID(Group.class, groupOID);

            if (group != null && group.getExecutionDegree() != null) {
                List finalDegreeWorkProposals = persistentFinalDegreeWork
                        .readPublishedFinalDegreeWorkProposalsByExecutionDegree(group
                                .getExecutionDegree().getIdInternal());

                if (finalDegreeWorkProposals != null) {
                    finalDegreeWorkProposalHeaders = new ArrayList();
                    for (int i = 0; i < finalDegreeWorkProposals.size(); i++) {
                        IProposal proposal = (Proposal) finalDegreeWorkProposals.get(i);

                        if (proposal != null
                                && !CollectionUtils.exists(group.getGroupProposals(),
                                        new PREDICATE_FIND_GROUP_PROPOSAL_BY_PROPOSAL(proposal))) {
                            FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader = new FinalDegreeWorkProposalHeader();

                            finalDegreeWorkProposalHeader.setIdInternal(proposal.getIdInternal());
                            finalDegreeWorkProposalHeader
                                    .setProposalNumber(proposal.getProposalNumber());
                            finalDegreeWorkProposalHeader.setTitle(proposal.getTitle());
                            finalDegreeWorkProposalHeader.setExecutionYear(proposal.getExecutionDegree().getExecutionYear().getYear());
                            if (proposal.getOrientator() != null) {
                                finalDegreeWorkProposalHeader.setOrientatorOID(proposal.getOrientator()
                                        .getIdInternal());
                                finalDegreeWorkProposalHeader.setOrientatorName(proposal.getOrientator()
                                        .getPerson().getNome());
                            }
                            if (proposal.getCoorientator() != null) {
                                finalDegreeWorkProposalHeader.setCoorientatorOID(proposal
                                        .getCoorientator().getIdInternal());
                                finalDegreeWorkProposalHeader.setCoorientatorName(proposal
                                        .getCoorientator().getPerson().getNome());
                            }
                            finalDegreeWorkProposalHeader.setCompanyLink(proposal.getCompanionName());
                            finalDegreeWorkProposalHeader.setStatus(proposal.getStatus());

                            finalDegreeWorkProposalHeaders.add(finalDegreeWorkProposalHeader);
                        }
                    }
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return finalDegreeWorkProposalHeaders;
    }

    private class PREDICATE_FIND_GROUP_PROPOSAL_BY_PROPOSAL implements Predicate {

        IProposal proposal;

        public boolean evaluate(Object arg0) {
            IGroupProposal groupProposal = (IGroupProposal) arg0;
            return proposal.equals(groupProposal.getFinalDegreeWorkProposal());
        }

        public PREDICATE_FIND_GROUP_PROPOSAL_BY_PROPOSAL(IProposal proposal) {
            super();
            this.proposal = proposal;
        }
    }

}