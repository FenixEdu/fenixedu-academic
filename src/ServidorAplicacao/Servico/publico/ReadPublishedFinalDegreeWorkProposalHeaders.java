/*
 * Created on 2004/04/04
 *  
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoBranch;
import DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader;
import Dominio.IBranch;
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
public class ReadPublishedFinalDegreeWorkProposalHeaders
	implements IService {

	public ReadPublishedFinalDegreeWorkProposalHeaders() {
		super();
	}

	public List run(Integer executionDegreeOID) throws FenixServiceException {
		List finalDegreeWorkProposalHeaders = new ArrayList();

		try {
			ISuportePersistente persistentSupport =
				SuportePersistenteOJB.getInstance();
			IPersistentFinalDegreeWork persistentFinalDegreeWork =
				persistentSupport.getIPersistentFinalDegreeWork();
			List finalDegreeWorkProposals =
				persistentFinalDegreeWork
					.readPublishedFinalDegreeWorkProposalsByExecutionDegree(
					executionDegreeOID);
			if (finalDegreeWorkProposals != null) {
				finalDegreeWorkProposalHeaders = new ArrayList();
				for (int i = 0; i < finalDegreeWorkProposals.size(); i++) {
					IProposal proposal =
						(Proposal) finalDegreeWorkProposals.get(i);

					if (proposal != null) {
						FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader =
							new FinalDegreeWorkProposalHeader();

						finalDegreeWorkProposalHeader.setIdInternal(
							proposal.getIdInternal());
						finalDegreeWorkProposalHeader.setProposalNumber(proposal.getProposalNumber());
						finalDegreeWorkProposalHeader.setTitle(
							proposal.getTitle());
						if (proposal.getOrientator() != null) {
							finalDegreeWorkProposalHeader.setOrientatorOID(
								proposal.getOrientator().getIdInternal());
							finalDegreeWorkProposalHeader.setOrientatorName(
								proposal.getOrientator().getPerson().getNome());
						}
						if (proposal.getCoorientator() != null) {
							finalDegreeWorkProposalHeader.setCoorientatorOID(
								proposal.getCoorientator().getIdInternal());
							finalDegreeWorkProposalHeader.setCoorientatorName(
								proposal
									.getCoorientator()
									.getPerson()
									.getNome());
						}
						finalDegreeWorkProposalHeader.setCompanyLink(
							proposal.getCompanionName());
						finalDegreeWorkProposalHeader.setStatus(proposal.getStatus());
						
						if (proposal.getBranches() != null) {
						    finalDegreeWorkProposalHeader.setBranches(new ArrayList());
						    for (int j = 0 ; j < proposal.getBranches().size(); j++) {
						        IBranch branch = (IBranch) proposal.getBranches().get(j);

						        if (branch != null) {
						            InfoBranch infoBranch = new InfoBranch();
						            infoBranch.setIdInternal(branch.getIdInternal());
						            infoBranch.setName(branch.getName());
						            finalDegreeWorkProposalHeader.getBranches().add(infoBranch);
						        }
						    }
						}

						finalDegreeWorkProposalHeaders.add(
							finalDegreeWorkProposalHeader);
					}
				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return finalDegreeWorkProposalHeaders;
	}
}
