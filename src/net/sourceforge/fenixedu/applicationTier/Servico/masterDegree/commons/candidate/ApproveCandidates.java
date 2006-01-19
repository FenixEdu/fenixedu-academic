package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ApproveCandidates implements IService {

    public void run(String[] situations, String[] ids, String[] remarks, String[] substitutes)
            throws ExcepcaoPersistencia {

        ISuportePersistente sp = null;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        for (int i = 0; i < situations.length; i++) {

            MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                            new Integer(ids[i]));
            CandidateSituation candidateSituationOld = masterDegreeCandidate
                    .getActiveCandidateSituation();

            CandidateSituation candidateSituationOldFromBD = (CandidateSituation) sp
                    .getIPersistentCandidateSituation().readByOID(CandidateSituation.class,
                            candidateSituationOld.getIdInternal());
            candidateSituationOldFromBD.setValidation(new State(State.INACTIVE));

            if ((substitutes[i] != null) && (substitutes[i].length() > 0)) {
                masterDegreeCandidate.setSubstituteOrder(new Integer(substitutes[i]));
            }

            // Create the new Candidate Situation

            CandidateSituation candidateSituation = DomainFactory.makeCandidateSituation();
            candidateSituation.setDate(Calendar.getInstance().getTime());
            candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
            candidateSituation.setRemarks(remarks[i]);
            candidateSituation.setSituation(new SituationName(situations[i]));
            candidateSituation.setValidation(new State(State.ACTIVE));

            // masterDegreeCandidate.getSituations().add(candidateSituation);

        }

    }
}