package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.ExecutionDegree;
import Dominio.ICandidateSituation;
import Dominio.IExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidatesForSelection implements IService {

    public List run(Integer executionDegreeID, List situations) throws FenixServiceException {

        ISuportePersistente sp = null;
        List resultTemp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            // Read the candidates

            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
            IExecutionDegree executionDegree = (ExecutionDegree) executionDegreeDAO.readByOID(
                    ExecutionDegree.class, executionDegreeID);

            resultTemp = sp.getIPersistentCandidateSituation().readActiveSituationsBySituationList(
                    executionDegree, situations);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);

            throw newEx;
        }

        if ((resultTemp == null) || (resultTemp.size() == 0)) {
            throw new NonExistingServiceException();
        }

        Iterator candidateIterator = resultTemp.iterator();
        List result = new ArrayList();
        while (candidateIterator.hasNext()) {
            ICandidateSituation candidateSituation = (ICandidateSituation) candidateIterator.next();
            result.add(Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(candidateSituation
                    .getMasterDegreeCandidate()));
        }

        return result;

    }

}