package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteDelegateVotingPeriod extends FenixService {

	@Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
	@Service
	public static void run(ElectionPeriodBean bean) throws FenixServiceException {
		try {
			DelegateElection election = bean.getElection();
			DelegateElectionVotingPeriod votingPeriod;
			if (election.hasVotingPeriod(bean.getStartDate(), bean.getEndDate())) {
				votingPeriod = election.getVotingPeriod(bean.getStartDate(), bean.getEndDate());
			} else {
				votingPeriod = election.getLastVotingPeriod();
			}
			election.deleteVotingPeriod(votingPeriod, bean.getRemoveCandidacyPeriod());
		} catch (DomainException ex) {
			throw new FenixServiceException(ex.getMessage(), ex.getArgs());
		}
	}

	@Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
	@Service
	public static void run(ElectionPeriodBean bean, String degreeOID) throws FenixServiceException {
		final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
		final Degree degree = rootDomainObject.readDegreeByOID(Integer.parseInt(degreeOID));

		DelegateElection election =
				degree.getYearDelegateElectionWithLastCandidacyPeriod(executionYear, bean.getCurricularYear());

		if (election != null) {
			bean.setElection(election);
		} else {
			throw new FenixServiceException("error.elections.edit.electionNotFound", new String[] { degree.getSigla(),
					bean.getCurricularYear().getYear().toString() });
		}

		run(bean);
	}
}