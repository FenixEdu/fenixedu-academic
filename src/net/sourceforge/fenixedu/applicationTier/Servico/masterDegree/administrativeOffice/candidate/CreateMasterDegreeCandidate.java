/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.GenerateUsername;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo </a>
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali </a>
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 */
public class CreateMasterDegreeCandidate implements IService {

	public InfoMasterDegreeCandidate run(Specialization degreeType, Integer executionDegreeID,
			String name, String identificationDocumentNumber, IDDocumentType identificationDocumentType)
			throws Exception {

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
		IPersistentMasterDegreeCandidate masterDegreeCandidateDAO = sp
				.getIPersistentMasterDegreeCandidate();

		// Read the Execution of this degree in the current execution Year
		IExecutionDegree executionDegree = (IExecutionDegree) executionDegreeDAO.readByOID(
				ExecutionDegree.class, executionDegreeID);

		IMasterDegreeCandidate masterDegreeCandidateFromDB = masterDegreeCandidateDAO
				.readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
						identificationDocumentNumber, identificationDocumentType, executionDegreeID,
						degreeType);

		if (masterDegreeCandidateFromDB != null) {
			throw new ExistingServiceException();
		}

		// Set the Candidate's Situation
		ICandidateSituation candidateSituation = new CandidateSituation();
		sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);
		// First candidate situation
		candidateSituation.setRemarks("Pré-Candidatura. Pagamento da candidatura por efectuar.");
		candidateSituation.setSituation(new SituationName(SituationName.PRE_CANDIDATO));
		candidateSituation.setValidation(new State(State.ACTIVE));
		Calendar actualDate = Calendar.getInstance();
		candidateSituation.setDate(actualDate.getTime());

		List situations = new ArrayList();
		situations.add(candidateSituation);

		// Create the Candidate
		IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();
		masterDegreeCandidateDAO.simpleLockWrite(masterDegreeCandidate);
		masterDegreeCandidate.setSituations(situations);
		candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
		masterDegreeCandidate.setSpecialization(degreeType);
		masterDegreeCandidate.setExecutionDegree(executionDegree);

		// Generate the Candidate's number
		Integer number = sp.getIPersistentMasterDegreeCandidate().generateCandidateNumber(
				executionDegree.getExecutionYear().getYear(),
				executionDegree.getDegreeCurricularPlan().getDegree().getSigla(), degreeType);
		masterDegreeCandidate.setCandidateNumber(number);

		// Check if the person Exists
		IPerson person = sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(
				identificationDocumentNumber, identificationDocumentType);

		if (person == null) {
			// Create the new Person
			person = new Person();
			sp.getIPessoaPersistente().simpleLockWrite(person);

			person.setNome(name);
			person.setNumeroDocumentoIdentificacao(identificationDocumentNumber);
			person.setIdDocumentType(identificationDocumentType);
			person.setGender(Gender.MALE);

			// Generate Person Username
			String username = GenerateUsername.getCandidateUsername(masterDegreeCandidate);
			person.setUsername(username);

			// Give the Person Role
			person.setPersonRoles(new ArrayList());
			person.getPersonRoles().add(sp.getIPersistentRole().readByRoleType(RoleType.PERSON));
		}

		if (!person.getPersonRoles().contains(
				sp.getIPersistentRole().readByRoleType(RoleType.MASTER_DEGREE_CANDIDATE))) {
			sp.getIPessoaPersistente().simpleLockWrite(person);
			person.getPersonRoles().add(
					sp.getIPersistentRole().readByRoleType(RoleType.MASTER_DEGREE_CANDIDATE));
		}
		masterDegreeCandidate.setPerson(person);

		// Return the new Candidate
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
				.newInfoFromDomain(masterDegreeCandidate);
		InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
				.newInfoFromDomain(masterDegreeCandidate.getExecutionDegree());
		infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);
		return infoMasterDegreeCandidate;
	}
}