/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateListByPersonAndExecutionDegree implements IService {

	public InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson,
			Integer number) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = null;
		MasterDegreeCandidate result = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		// Read the candidates

		Person person = (Person) sp.getIPessoaPersistente().readByOID(Person.class,
				infoPerson.getIdInternal());

		result = sp.getIPersistentMasterDegreeCandidate().readByExecutionDegreeAndPersonAndNumber(
				infoExecutionDegree.getIdInternal(), person.getIdInternal(), number);

		InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
				.newInfoFromDomain(result);
		Iterator situationIterator = result.getSituations().iterator();
		List situations = new ArrayList();
		while (situationIterator.hasNext()) {
			InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
					.newInfoFromDomain((CandidateSituation) situationIterator.next());
			situations.add(infoCandidateSituation);

			// Check if this is the Active Situation
			if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
				infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
		}
		infoMasterDegreeCandidate.setSituationList(situations);

		return infoMasterDegreeCandidate;

	}

}