package ServidorAplicacao.Servico.commons.degree;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadNumerusClausus implements IService {

    /**
     *  
     */
    public ReadNumerusClausus() {

    }

    public Integer run(Integer executionDegreeID)
            throws NonExistingServiceException {

        ICursoExecucao executionDegree = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ICursoExecucaoPersistente executionDegreeDAO = sp
                    .getICursoExecucaoPersistente();
            executionDegree = (CursoExecucao) executionDegreeDAO.readByOID(
                    CursoExecucao.class, executionDegreeID);

        } catch (ExcepcaoPersistencia ex) {
            throw new RuntimeException(ex);
        }

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        return executionDegree.getCurricularPlan().getNumerusClausus();
    }

}