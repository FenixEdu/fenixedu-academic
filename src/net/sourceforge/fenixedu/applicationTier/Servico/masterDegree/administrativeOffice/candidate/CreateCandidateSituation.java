package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo </a>
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali </a>
 *
 */
public class CreateCandidateSituation implements IService {

	public void run(Integer executionDegreeID, Integer personID, SituationName newSituation)
			throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IMasterDegreeCandidate masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
				.readByExecutionDegreeAndPerson(executionDegreeID, personID);
		if (masterDegreeCandidate == null) {
			throw new ExcepcaoInexistente("Unknown Master Degree Candidate");
		}

		for (ICandidateSituation candidateSituation : (List<ICandidateSituation>) masterDegreeCandidate
				.getSituations()) {
			if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {
				sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);
				candidateSituation.setValidation(new State(State.INACTIVE));
			}
		}

		// Create the New Candidate Situation
		ICandidateSituation candidateSituation = new CandidateSituation();
		sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);
		Calendar calendar = Calendar.getInstance();
		candidateSituation.setDate(calendar.getTime());
		candidateSituation.setSituation(newSituation);
		candidateSituation.setValidation(new State(State.ACTIVE));
		candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);

		// Update Candidate
		sp.getIPersistentMasterDegreeCandidate().simpleLockWrite(masterDegreeCandidate);
		masterDegreeCandidate.getSituations().add(candidateSituation);

	}

}