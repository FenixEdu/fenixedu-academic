package ServidorAplicacao.Servico.commons.degree;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CursoExecucao;
import Dominio.ExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadNumerusClausus implements IService
{

    /**
     * 
     */
    public ReadNumerusClausus()
    {
        
    }

    public Integer run(String executionYearString, String degreeCode) throws NonExistingServiceException
    {

        ICursoExecucao executionDegree = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionYear executionYear = new ExecutionYear();
            executionYear.setYear(executionYearString);
            executionDegree = sp.getICursoExecucaoPersistente().readByDegreeCodeAndExecutionYear(
                    degreeCode, executionYear);

        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new RuntimeException(ex);
        }

        if (executionDegree == null) { throw new NonExistingServiceException(); }

        return executionDegree.getCurricularPlan().getNumerusClausus();
    }

    public Integer run(Integer executionDegreeID) throws NonExistingServiceException
    {

        ICursoExecucao executionDegree = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ICursoExecucao executionDegreeDomain = new CursoExecucao();
            executionDegreeDomain.setIdInternal(executionDegreeID);

            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
            executionDegree = (CursoExecucao) executionDegreeDAO.readByOId(executionDegreeDomain, false);

        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new RuntimeException(ex);
        }

        if (executionDegree == null) { throw new NonExistingServiceException(); }

        return executionDegree.getCurricularPlan().getNumerusClausus();
    }

   
}