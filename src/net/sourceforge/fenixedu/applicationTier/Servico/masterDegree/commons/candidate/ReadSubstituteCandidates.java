package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.SituationName;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadSubstituteCandidates implements IService {

    public List run(String[] candidateList, String[] ids) throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = new ArrayList();

        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the substitute candidates
            int size = candidateList.length;

            for (int i = 0; i < size; i++) {
                if (candidateList[i].equals(SituationName.SUPLENTE_STRING)
                        || candidateList[i]
                                .equals(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING)
                        || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING)
                        || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING)) {

                    Integer idInternal = new Integer(ids[i]);

                    IMasterDegreeCandidate masterDegreeCandidateToWrite = (IMasterDegreeCandidate) sp
                            .getIPersistentMasterDegreeCandidate().readByOID(
                                    MasterDegreeCandidate.class, idInternal);
                    result
                            .add(Cloner
                                    .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidateToWrite));
                }
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);

            throw newEx;
        }

        return result;

    }

}