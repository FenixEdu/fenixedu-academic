package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationName;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ApproveCandidates implements IService {

    /**
     * The actor of this class.
     */
    public ApproveCandidates() {
    }

    public void run(String[] situations, String[] ids, String[] remarks, String[] substitutes)
            throws FenixServiceException {

        ISuportePersistente sp = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            for (int i = 0; i < situations.length; i++) {

                IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                        .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                                new Integer(ids[i]));
                ICandidateSituation candidateSituationOld = masterDegreeCandidate
                        .getActiveCandidateSituation();

                ICandidateSituation candidateSituationOldFromBD = (ICandidateSituation) sp
                        .getIPersistentCandidateSituation().readByOID(CandidateSituation.class,
                                candidateSituationOld.getIdInternal(), true);
                candidateSituationOldFromBD.setValidation(new State(State.INACTIVE));

                if ((substitutes[i] != null) && (substitutes[i].length() > 0)) {
                    masterDegreeCandidate.setSubstituteOrder(new Integer(substitutes[i]));
                }

                // Create the new Candidate Situation

                ICandidateSituation candidateSituation = new CandidateSituation();
                sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);
                candidateSituation.setDate(Calendar.getInstance().getTime());
                candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
                candidateSituation.setRemarks(remarks[i]);
                candidateSituation.setSituation(new SituationName(situations[i]));
                candidateSituation.setValidation(new State(State.ACTIVE));

                //				masterDegreeCandidate.getSituations().add(candidateSituation);

            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}