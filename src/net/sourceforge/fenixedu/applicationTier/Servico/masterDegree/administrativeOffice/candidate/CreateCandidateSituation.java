package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.applicationTier.IService;

public class CreateCandidateSituation implements IService {

    public void run(Integer executionDegreeID, Integer personID, SituationName newSituation)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final MasterDegreeCandidate masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                .readByExecutionDegreeAndPerson(executionDegreeID, personID);
        if (masterDegreeCandidate == null) {
            throw new ExcepcaoInexistente("Unknown Master Degree Candidate");
        }

        for (CandidateSituation candidateSituation : masterDegreeCandidate.getSituations()) {
            if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {
                candidateSituation.setValidation(new State(State.INACTIVE));
            }
        }

        // Create the New Candidate Situation
        CandidateSituation candidateSituation = DomainFactory.makeCandidateSituation();
        Calendar calendar = Calendar.getInstance();
        candidateSituation.setDate(calendar.getTime());
        candidateSituation.setSituation(newSituation);
        candidateSituation.setValidation(new State(State.ACTIVE));
        candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);

        // Update Candidate
        masterDegreeCandidate.getSituations().add(candidateSituation);

    }

}
