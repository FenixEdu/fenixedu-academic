package ServidorAplicacao.Servico.commons.degree;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByCandidateID implements IService {

    public InfoExecutionDegree run(Integer candidateID) throws NonExistingServiceException {

        ICursoExecucao executionDegree = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                            candidateID);

            executionDegree = (ICursoExecucao) sp.getIPersistentExecutionDegree().readByOID(
                    CursoExecucao.class, masterDegreeCandidate.getExecutionDegree().getIdInternal());

        } catch (ExcepcaoPersistencia ex) {
            throw new RuntimeException(ex);
        }

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        return (InfoExecutionDegree) Cloner.get(executionDegree);
    }

}