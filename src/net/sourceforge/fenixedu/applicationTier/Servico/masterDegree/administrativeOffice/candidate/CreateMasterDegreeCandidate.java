/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.person.Sex;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt) modified by Fernanda Quitério
 * 
 */
public class CreateMasterDegreeCandidate implements IService {

    public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate newMasterDegreeCandidate)
            throws Exception {

        IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();

        ISuportePersistente sp = null;

        IPerson person = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
            IPersistentMasterDegreeCandidate masterDegreeDAO = sp.getIPersistentMasterDegreeCandidate();

            // Read the Execution of this degree in the current execution Year
            IExecutionDegree executionDegree = (IExecutionDegree) executionDegreeDAO.readByOID(
                    ExecutionDegree.class, newMasterDegreeCandidate.getInfoExecutionDegree()
                            .getIdInternal());

            IMasterDegreeCandidate masterDegreeCandidateFromDB = masterDegreeDAO
                    .readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
                            newMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(),
                            newMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao()
                                    .getTipo(), executionDegree, newMasterDegreeCandidate
                                    .getSpecialization());

            if (masterDegreeCandidateFromDB != null) {
                throw new ExistingServiceException();
            }

            // Check if the person Exists
            person = sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(
                    newMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(),
                    newMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao());

            if (person != null) {
                sp.getIPessoaPersistente().simpleLockWrite(person);
            }

            // Create the Candidate

            // Set the Candidate's Situation
            List situations = new ArrayList();
            ICandidateSituation candidateSituation = new CandidateSituation();

            sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);
            // sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);

            // First candidate situation
            candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
            candidateSituation.setRemarks("Pré-Candidatura. Pagamento da candidatura por efectuar.");
            candidateSituation.setSituation(new SituationName(SituationName.PRE_CANDIDATO));
            candidateSituation.setValidation(new State(State.ACTIVE));

            Calendar actualDate = Calendar.getInstance();
            candidateSituation.setDate(actualDate.getTime());

            situations.add(candidateSituation);

            masterDegreeDAO.simpleLockWrite(masterDegreeCandidate);

            masterDegreeCandidate.setSituations(situations);
            masterDegreeCandidate.setPerson(person);
            masterDegreeCandidate.setSpecialization(newMasterDegreeCandidate.getSpecialization());
            masterDegreeCandidate.setExecutionDegree(executionDegree);

            // Generate the Candidate's number
            Integer number = sp.getIPersistentMasterDegreeCandidate().generateCandidateNumber(
                    masterDegreeCandidate.getExecutionDegree().getExecutionYear().getYear(),
                    masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                            .getSigla(), masterDegreeCandidate.getSpecialization());

            masterDegreeCandidate.setCandidateNumber(number);

            if (person == null) {
                // Create the new Person
                person = new Person();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.setNome(newMasterDegreeCandidate.getInfoPerson().getNome());
                person.setNumeroDocumentoIdentificacao(newMasterDegreeCandidate.getInfoPerson()
                        .getNumeroDocumentoIdentificacao());
                person.setTipoDocumentoIdentificacao(newMasterDegreeCandidate.getInfoPerson()
                        .getTipoDocumentoIdentificacao());
                person.setSex(Sex.MALE);

                // Generate Person Username
                String username = GenerateUsername.getCandidateUsername(masterDegreeCandidate);

                person.setUsername(username);

                // Give the Person Role
                person.setPersonRoles(new ArrayList());
                person.getPersonRoles().add(sp.getIPersistentRole().readByRoleType(RoleType.PERSON));
            }
            if (!person.getPersonRoles().contains(
                    sp.getIPersistentRole().readByRoleType(RoleType.MASTER_DEGREE_CANDIDATE))) {
                person.getPersonRoles().add(
                        sp.getIPersistentRole().readByRoleType(RoleType.MASTER_DEGREE_CANDIDATE));
            }
            masterDegreeCandidate.setPerson(person);

            // Write the new Candidate
            // sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(masterDegreeCandidate);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        // Return the new Candidate
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate);
        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan.newInfoFromDomain(masterDegreeCandidate.getExecutionDegree());
        infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);
        return infoMasterDegreeCandidate;
    }
}