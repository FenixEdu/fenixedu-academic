package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PrintAllCandidatesFilter;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public class ReadDegreeCandidatesWithFilter extends Service {

    public List run(Integer degreeCurricularPlanId, PrintAllCandidatesFilter filterBy, String filterValue)
            throws FenixServiceException, ExcepcaoPersistencia {
        
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        return createInfoMasterDegreeCandidateFromDomain(getMasterDegreeCandidates(degreeCurricularPlan, filterBy, filterValue));
    }

    private Set<MasterDegreeCandidate> getMasterDegreeCandidates(DegreeCurricularPlan degreeCurricularPlan,
            PrintAllCandidatesFilter filterBy, String filterValue) {

        switch (filterBy) {
        case FILTERBY_SPECIALIZATION_VALUE:
            return degreeCurricularPlan.readMasterDegreeCandidatesBySpecialization(Specialization
                    .valueOf(filterValue));

        case FILTERBY_SITUATION_VALUE:
            return degreeCurricularPlan.readMasterDegreeCandidatesBySituatioName(new SituationName(
                    filterValue));

        case FILTERBY_GIVESCLASSES_VALUE:
            return degreeCurricularPlan.readMasterDegreeCandidatesByCourseAssistant(true);

        case FILTERBY_DOESNTGIVESCLASSES_VALUE:
            return degreeCurricularPlan.readMasterDegreeCandidatesByCourseAssistant(false);

        default:
            return null;
        }
    }

    private List createInfoMasterDegreeCandidateFromDomain(Set<MasterDegreeCandidate> masterDegreeCandidates) {
        
        final State candidateSituationState = new State(State.ACTIVE);
        final List<InfoMasterDegreeCandidate> result = new ArrayList<InfoMasterDegreeCandidate>();

        for (final MasterDegreeCandidate masterDegreeCandidate : masterDegreeCandidates) {
            
            final InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);
            
            final List<InfoCandidateSituation> candidateSituations = new ArrayList<InfoCandidateSituation>();
            for (final CandidateSituation candidateSituation : masterDegreeCandidate.getSituationsSet()) {

                final InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation.newInfoFromDomain(candidateSituation);
                candidateSituations.add(infoCandidateSituation);

                if (candidateSituation.getValidation().equals(candidateSituationState)) {
                    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
                }
            }
            
            infoMasterDegreeCandidate.setSituationList(candidateSituations);
            result.add(infoMasterDegreeCandidate);
        }
        return result;
    }
}