package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICandidateSituation;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidatesForSelection implements IServico {

    private static ReadCandidatesForSelection servico = new ReadCandidatesForSelection();

    /**
     * The singleton access method of this class.
     */
    public static ReadCandidatesForSelection getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadCandidatesForSelection() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "ReadCandidatesForSelection";
    }

    /*
     * public List run(String executionYearString, String degree, List
     * situations) throws FenixServiceException {
     * 
     * ISuportePersistente sp = null; List resultTemp = null;
     * 
     * try { sp = SuportePersistenteOJB.getInstance();
     *  // Read the candidates IExecutionYear executionYear = new
     * ExecutionYear();
     * 
     * executionYear.setYear(executionYearString);
     * 
     * ICursoExecucao executionDegree =
     * sp.getICursoExecucaoPersistente().readByDegreeCodeAndExecutionYear(degree,
     * executionYear);
     * 
     * resultTemp =
     * sp.getIPersistentCandidateSituation().readActiveSituationsBySituationList(executionDegree,
     * situations);
     *  } catch (ExcepcaoPersistencia ex) { FenixServiceException newEx = new
     * FenixServiceException("Persistence layer error");
     * newEx.fillInStackTrace(); throw newEx; }
     * 
     * if ((resultTemp == null) || (resultTemp.size() == 0 )) { throw new
     * NonExistingServiceException(); }
     * 
     * Iterator candidateIterator = resultTemp.iterator(); List result = new
     * ArrayList(); while (candidateIterator.hasNext()){ ICandidateSituation
     * candidateSituation = (ICandidateSituation) candidateIterator.next();
     * result.add(Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(candidateSituation.getMasterDegreeCandidate())); }
     * 
     * return result;
     *  }
     */

    public List run(Integer executionDegreeID, List situations)
            throws FenixServiceException {

        ISuportePersistente sp = null;
        List resultTemp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            // Read the candidates

            ICursoExecucaoPersistente executionDegreeDAO = sp
                    .getICursoExecucaoPersistente();
            ICursoExecucao executionDegree = (CursoExecucao) executionDegreeDAO
                    .readByOID(CursoExecucao.class, executionDegreeID);

            resultTemp = sp.getIPersistentCandidateSituation()
                    .readActiveSituationsBySituationList(executionDegree,
                            situations);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error", ex);

            throw newEx;
        }

        if ((resultTemp == null) || (resultTemp.size() == 0)) {
            throw new NonExistingServiceException();
        }

        Iterator candidateIterator = resultTemp.iterator();
        List result = new ArrayList();
        while (candidateIterator.hasNext()) {
            ICandidateSituation candidateSituation = (ICandidateSituation) candidateIterator
                    .next();
            result
                    .add(Cloner
                            .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(candidateSituation
                                    .getMasterDegreeCandidate()));
        }

        return result;

    }

}