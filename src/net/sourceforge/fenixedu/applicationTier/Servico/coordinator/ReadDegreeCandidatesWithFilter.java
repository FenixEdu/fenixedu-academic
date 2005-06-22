package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PrintAllCandidatesFilter;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Clerigo & Telmo Nabais
 * 
 */
public class ReadDegreeCandidatesWithFilter implements IService {

    private Specialization filterSpecialization = null;

    private SituationName filterSituationName = null;

    private Boolean filterGivesClasses = null;

    public List run(Integer degreeCurricularPlanId, PrintAllCandidatesFilter filterBy, String filterValue)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        initializeFilterValues(filterBy, filterValue);

        List candidates = sp.getIPersistentMasterDegreeCandidate()
                .readAllCandidatesByDCPlanIDSpecSituationAndIsAssistant(degreeCurricularPlanId,
                        filterSpecialization, filterSituationName, filterGivesClasses);

        if (candidates == null)
            return new ArrayList();

        return createInfoMasterDegreeCandidateFromDomain(candidates);
    }

    private void initializeFilterValues(PrintAllCandidatesFilter filterBy, String filterValue) {
        filterSpecialization = null;
        filterGivesClasses = null;
        filterSituationName = null;

        switch (filterBy) {
        case FILTERBY_SPECIALIZATION_VALUE:
            filterSpecialization = Specialization.valueOf(filterValue);
            break;
        case FILTERBY_SITUATION_VALUE:
            filterSituationName = new SituationName(filterValue);
            break;
        case FILTERBY_GIVESCLASSES_VALUE:
            filterGivesClasses = new Boolean(true);
            break;
        case FILTERBY_DOESNTGIVESCLASSES_VALUE:
            filterGivesClasses = new Boolean(false);
            break;
        }
    }

    private List createInfoMasterDegreeCandidateFromDomain(List candidates) {
        Iterator iterator = candidates.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            // For all candidates ...
            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator.next();
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);
            // Copy all Situations
            List situations = new ArrayList();
            Iterator situationIter = masterDegreeCandidate.getSituations().iterator();
            while (situationIter.hasNext()) {

                InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
                        .newInfoFromDomain((ICandidateSituation) situationIter.next());
                situations.add(infoCandidateSituation);

                // Check if this is the Active Situation
                if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
            }
            infoMasterDegreeCandidate.setSituationList(situations);
            result.add(infoMasterDegreeCandidate);
        }
        return result;
    }
}