package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationName;

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