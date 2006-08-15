/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
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

        // Read the Execution of this degree in the current execution Year
        final ExecutionDegree executionDegree = rootDomainObject
                .readExecutionDegreeByOID(executionDegreeID);
        Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationDocumentNumber,
                identificationDocumentType);

        if (person == null) {
            // Create the new Person
            person = new Person(name, identificationDocumentNumber,
                    identificationDocumentType, Gender.MALE, "T" + System.currentTimeMillis());
        } else {
            MasterDegreeCandidate existingMasterDegreeCandidate = person
                    .getMasterDegreeCandidateByExecutionDegree(executionDegree);
            if (existingMasterDegreeCandidate != null) {
                throw new ExistingServiceException();
            }
        }

        person.addPersonRoleByRoleType(RoleType.MASTER_DEGREE_CANDIDATE);
        person.addPersonRoleByRoleType(RoleType.PERSON);
        

        // Set the Candidate's Situation
        CandidateSituation candidateSituation = new CandidateSituation();
        // First candidate situation
        candidateSituation.setRemarks("Pré-Candidatura. Pagamento da candidatura por efectuar.");
        candidateSituation.setSituation(new SituationName(SituationName.PRE_CANDIDATO));
        candidateSituation.setValidation(new State(State.ACTIVE));
        Calendar actualDate = Calendar.getInstance();
        candidateSituation.setDate(actualDate.getTime());

        // Create the Candidate
        MasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();
        masterDegreeCandidate.addSituations(candidateSituation);
        masterDegreeCandidate.setSpecialization(degreeType);
        masterDegreeCandidate.setExecutionDegree(executionDegree);
        masterDegreeCandidate.setCandidateNumber(executionDegree
                .generateCandidateNumberForSpecialization(degreeType));

        masterDegreeCandidate.setPerson(person);

        // Return the new Candidate
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate);
        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                .newInfoFromDomain(masterDegreeCandidate.getExecutionDegree());
        infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);
        return infoMasterDegreeCandidate;
    }
}
