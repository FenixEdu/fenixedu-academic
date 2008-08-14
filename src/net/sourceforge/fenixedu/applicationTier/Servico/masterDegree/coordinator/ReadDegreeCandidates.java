package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.util.State;

public class ReadDegreeCandidates extends Service {

    public List run(InfoExecutionDegree infoExecutionDegree) {

	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
	return createInfoMasterDegreeCandidates(executionDegree.getMasterDegreeCandidatesSet());
    }

    public List run(Integer degreeCurricularPlanId) {
	final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
	return createInfoMasterDegreeCandidates(degreeCurricularPlan.readMasterDegreeCandidates());
    }

    private List createInfoMasterDegreeCandidates(final Set<MasterDegreeCandidate> masterDegreeCandidates) {
	final State activeCandidateSituationState = new State(State.ACTIVE);
	final List<InfoMasterDegreeCandidate> result = new ArrayList<InfoMasterDegreeCandidate>();

	for (final MasterDegreeCandidate masterDegreeCandidate : masterDegreeCandidates) {
	    InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
		    .newInfoFromDomain(masterDegreeCandidate);

	    final List<InfoCandidateSituation> infoCandidateSituations = new ArrayList<InfoCandidateSituation>();
	    for (final CandidateSituation candidateSituation : masterDegreeCandidate.getSituationsSet()) {
		final InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
			.newInfoFromDomain(candidateSituation);
		infoCandidateSituations.add(infoCandidateSituation);

		if (candidateSituation.getValidation().equals(activeCandidateSituationState)) {
		    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
		}
	    }
	    infoMasterDegreeCandidate.setSituationList(infoCandidateSituations);
	    result.add(infoMasterDegreeCandidate);
	}
	return result;
    }
}
