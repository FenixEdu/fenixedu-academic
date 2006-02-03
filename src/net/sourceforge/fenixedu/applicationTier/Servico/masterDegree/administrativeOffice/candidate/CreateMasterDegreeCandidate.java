/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

/**
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo </a>
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali </a>
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 */
public class CreateMasterDegreeCandidate extends Service {

    public InfoMasterDegreeCandidate run(Specialization degreeType, Integer executionDegreeID,
            String name, String identificationDocumentNumber, IDDocumentType identificationDocumentType)
            throws Exception {

        IPersistentMasterDegreeCandidate masterDegreeCandidateDAO = persistentSupport
                .getIPersistentMasterDegreeCandidate();

        // Read the Execution of this degree in the current execution Year
        ExecutionDegree executionDegree = (ExecutionDegree) persistentObject.readByOID(
                ExecutionDegree.class, executionDegreeID);

        MasterDegreeCandidate masterDegreeCandidateFromDB = masterDegreeCandidateDAO
                .readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
                        identificationDocumentNumber, identificationDocumentType, executionDegreeID,
                        degreeType);

        if (masterDegreeCandidateFromDB != null) {
            throw new ExistingServiceException();
        }

        // Set the Candidate's Situation
        CandidateSituation candidateSituation = DomainFactory.makeCandidateSituation();
        // First candidate situation
        candidateSituation.setRemarks("Pré-Candidatura. Pagamento da candidatura por efectuar.");
        candidateSituation.setSituation(new SituationName(SituationName.PRE_CANDIDATO));
        candidateSituation.setValidation(new State(State.ACTIVE));
        Calendar actualDate = Calendar.getInstance();
        candidateSituation.setDate(actualDate.getTime());

        // Create the Candidate
        MasterDegreeCandidate masterDegreeCandidate = DomainFactory.makeMasterDegreeCandidate();
        masterDegreeCandidate.addSituations(candidateSituation);
        candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
        masterDegreeCandidate.setSpecialization(degreeType);
        masterDegreeCandidate.setExecutionDegree(executionDegree);

        // Generate the Candidate's number
        Integer number = persistentSupport.getIPersistentMasterDegreeCandidate().generateCandidateNumber(
                executionDegree.getExecutionYear().getYear(),
                executionDegree.getDegreeCurricularPlan().getDegree().getSigla(), degreeType);
        masterDegreeCandidate.setCandidateNumber(number);

        // Check if the person Exists
        Person person = persistentSupport.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(
                identificationDocumentNumber, identificationDocumentType);

        List<Person> persons = (List<Person>) persistentSupport.getIPessoaPersistente().readAll(Person.class);

        Role personRole = Role.getRoleByRoleType(RoleType.PERSON);

        if (person == null) {
            // Create the new Person
            person = DomainFactory.makePerson(name, identificationDocumentNumber,
                    identificationDocumentType, Gender.MALE);

            // Generate Person Username
            String username = MasterDegreeCandidate.generateUsernameForNewCandidate(
                    masterDegreeCandidate, persons);
            person.changeUsername(username, persons);

            // Give the Person Role
            person.getPersonRoles().add(personRole);
        } else {
            if (person.getUsername().startsWith("INA")) {
                // Generate Person Username
                String username = MasterDegreeCandidate.generateUsernameForNewCandidate(
                        masterDegreeCandidate, persons);
                person.changeUsername(username, persons);
            }
            if (person.getUsername().startsWith("B")) {
                // Generate Person Username
                person.getPersonRoles().add(personRole);
                String username = MasterDegreeCandidate.generateUsernameForNewCandidate(
                        masterDegreeCandidate, persons);
                person.changeUsername(username, persons);
            }
        }

        if (!person.hasRole(RoleType.PERSON)) {
            person.addPersonRoles(personRole);
        }

        if (!person.hasRole(RoleType.MASTER_DEGREE_CANDIDATE)) {
            person.addPersonRoles(Role.getRoleByRoleType(
                    RoleType.MASTER_DEGREE_CANDIDATE));
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
