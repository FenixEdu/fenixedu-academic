/*
 * Created on 14/Mar/2003
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.CandidateSituation;
import Dominio.CursoExecucao;
import Dominio.ICandidateSituation;
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.MasterDegreeCandidate;
import Dominio.Pessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.person.GenerateUsername;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.RoleType;
import Util.SituationName;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt) modified by Fernanda Quitério
 *  
 */
public class CreateMasterDegreeCandidate implements IServico {

    private static CreateMasterDegreeCandidate servico = new CreateMasterDegreeCandidate();

    /**
     * The singleton access method of this class.
     */
    public static CreateMasterDegreeCandidate getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private CreateMasterDegreeCandidate() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "CreateMasterDegreeCandidate";
    }

    public InfoMasterDegreeCandidate run(
            InfoMasterDegreeCandidate newMasterDegreeCandidate)
            throws Exception {

        IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();

        ISuportePersistente sp = null;

        IPessoa person = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente executionDegreeDAO = sp
                    .getICursoExecucaoPersistente();
            IPersistentMasterDegreeCandidate masterDegreeDAO = sp
                    .getIPersistentMasterDegreeCandidate();

            // Read the Execution of this degree in the current execution Year
            ICursoExecucao executionDegree = (ICursoExecucao) executionDegreeDAO
                    .readByOID(CursoExecucao.class, newMasterDegreeCandidate
                            .getInfoExecutionDegree().getIdInternal());

            IMasterDegreeCandidate masterDegreeCandidateFromDB = masterDegreeDAO
                    .readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
                            newMasterDegreeCandidate.getInfoPerson()
                                    .getNumeroDocumentoIdentificacao(),
                            newMasterDegreeCandidate.getInfoPerson()
                                    .getTipoDocumentoIdentificacao().getTipo(),
                            executionDegree, newMasterDegreeCandidate
                                    .getSpecialization());

            if (masterDegreeCandidateFromDB != null) {
                throw new ExistingServiceException();
            }

            // Check if the person Exists
            person = sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(
                    newMasterDegreeCandidate.getInfoPerson()
                            .getNumeroDocumentoIdentificacao(),
                    newMasterDegreeCandidate.getInfoPerson()
                            .getTipoDocumentoIdentificacao());

            if (person != null) {
                sp.getIPessoaPersistente().simpleLockWrite(person);
            }

            // Create the Candidate

            // Set the Candidate's Situation
            List situations = new ArrayList();
            ICandidateSituation candidateSituation = new CandidateSituation();

            sp.getIPersistentCandidateSituation().simpleLockWrite(
                    candidateSituation);
            //			sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);

            // First candidate situation
            candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
            candidateSituation
                    .setRemarks("Pré-Candidatura. Pagamento da candidatura por efectuar.");
            candidateSituation.setSituation(new SituationName(
                    SituationName.PRE_CANDIDATO));
            candidateSituation.setValidation(new State(State.ACTIVE));

            Calendar actualDate = Calendar.getInstance();
            candidateSituation.setDate(actualDate.getTime());

            situations.add(candidateSituation);

            masterDegreeDAO.simpleLockWrite(masterDegreeCandidate);

            masterDegreeCandidate.setSituations(situations);
            masterDegreeCandidate.setPerson(person);
            masterDegreeCandidate.setSpecialization(newMasterDegreeCandidate
                    .getSpecialization());
            masterDegreeCandidate.setExecutionDegree(executionDegree);

            // Generate the Candidate's number
            Integer number = sp
                    .getIPersistentMasterDegreeCandidate()
                    .generateCandidateNumber(
                            masterDegreeCandidate.getExecutionDegree()
                                    .getExecutionYear().getYear(),
                            masterDegreeCandidate.getExecutionDegree()
                                    .getCurricularPlan().getDegree().getSigla(),
                            masterDegreeCandidate.getSpecialization());

            masterDegreeCandidate.setCandidateNumber(number);

            if (person == null) {
                // Create the new Person
                person = new Pessoa();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.setNome(newMasterDegreeCandidate.getInfoPerson()
                        .getNome());
                person.setNumeroDocumentoIdentificacao(newMasterDegreeCandidate
                        .getInfoPerson().getNumeroDocumentoIdentificacao());
                person.setTipoDocumentoIdentificacao(newMasterDegreeCandidate
                        .getInfoPerson().getTipoDocumentoIdentificacao());

                // Generate Person Username
                String username = GenerateUsername
                        .getCandidateUsername(masterDegreeCandidate);

                person.setUsername(username);

                // Give the Person Role
                person.setPersonRoles(new ArrayList());
                person.getPersonRoles()
                        .add(
                                sp.getIPersistentRole().readByRoleType(
                                        RoleType.PERSON));
            }
            if (!person.getPersonRoles().contains(
                    sp.getIPersistentRole().readByRoleType(
                            RoleType.MASTER_DEGREE_CANDIDATE))) {
                person.getPersonRoles().add(
                        sp.getIPersistentRole().readByRoleType(
                                RoleType.MASTER_DEGREE_CANDIDATE));
            }
            masterDegreeCandidate.setPerson(person);

            // Write the new Candidate
            //sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(masterDegreeCandidate);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        // Return the new Candidate
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner
                .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
        return infoMasterDegreeCandidate;
    }
}