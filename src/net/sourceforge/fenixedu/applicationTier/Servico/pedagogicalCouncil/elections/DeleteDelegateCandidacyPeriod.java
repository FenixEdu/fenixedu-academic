package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteDelegateCandidacyPeriod extends FenixService {

	@Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
	@Service
	public static void run(ElectionPeriodBean bean) throws FenixServiceException {
		try {
			DelegateElection election = bean.getElection();
			election.deleteCandidacyPeriod();
		} catch (DomainException ex) {
			throw new FenixServiceException(ex.getMessage(), ex.getArgs());
		}
	}

	@Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
	@Service
	public static void run(ElectionPeriodBean bean, String degreeOID) throws FenixServiceException {
		final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

		Degree degree = rootDomainObject.readDegreeByOID(Integer.parseInt(degreeOID));

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