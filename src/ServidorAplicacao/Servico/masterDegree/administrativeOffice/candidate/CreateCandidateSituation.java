package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.Calendar;
import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoPerson;
import DataBeans.util.Cloner;
import Dominio.CandidateSituation;
import Dominio.ExecutionDegree;
import Dominio.ICandidateSituation;
import Dominio.IExecutionDegree;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPerson;
import Dominio.MasterDegreeCandidate;
import Dominio.Person;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationName;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *         This Service Changes the Candidate Situation from Pre-Candidate to
 *         Pending
 */

public class CreateCandidateSituation implements IService {

    /**
     * The actor of this class.
     */
    public CreateCandidateSituation() {
    }

    // FIXME: Should this receive the new Situation ?

    public void run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson)
            throws FenixServiceException {

        IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
            if (infoExecutionDegree.getIdInternal() != null) {
                executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                        ExecutionDegree.class, infoExecutionDegree.getIdInternal());
            }

            IPerson person = Cloner.copyInfoPerson2IPerson(infoPerson);
            if (infoPerson.getIdInternal() != null) {
                person = (IPerson) persistentPerson.readByOID(Person.class, infoPerson.getIdInternal());
            }

            masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                    .readByExecutionDegreeAndPerson(executionDegree, person);
            if (masterDegreeCandidate == null) {
                throw new ExcepcaoInexistente("Unknown Master Degree Candidate !!");
            }
            sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(masterDegreeCandidate);
            Iterator iterator = masterDegreeCandidate.getSituations().iterator();
            while (iterator.hasNext()) {
                ICandidateSituation candidateSituation = (ICandidateSituation) iterator.next();
                if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {

                    ICandidateSituation candidateSituationFromBD = (ICandidateSituation) sp
                            .getIPersistentCandidateSituation().readByOID(CandidateSituation.class,
                                    candidateSituation.getIdInternal(), true);
                    candidateSituationFromBD.setValidation(new State(State.INACTIVE));
                }
            }

            // Create the New Candidate Situation
            ICandidateSituation candidateSituation = new CandidateSituation();
            sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);
            sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);
            Calendar calendar = Calendar.getInstance();
            candidateSituation.setDate(calendar.getTime());
            candidateSituation.setSituation(new SituationName(SituationName.PENDENTE_STRING));
            candidateSituation.setValidation(new State(State.ACTIVE));
            candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);

        } catch (ExistingPersistentException ex) {
            // The situation Already Exists ... Something wrong ?
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }
    }

}