/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateListByPersonAndExecutionDegree extends Service {

	public InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson,
			Integer number) throws FenixServiceException, ExcepcaoPersistencia {

		MasterDegreeCandidate result = null;

		// Read the candidates

		Person person = (Person) persistentObject.readByOID(Person.class,
				infoPerson.getIdInternal());

		result = persistentSupport.getIPersistentMasterDegreeCandidate().readByExecutionDegreeAndPersonAndNumber(
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