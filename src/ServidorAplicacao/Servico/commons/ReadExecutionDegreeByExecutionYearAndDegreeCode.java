package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.ExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadExecutionDegreeByExecutionYearAndDegreeCode implements IServico
{

    private static ReadExecutionDegreeByExecutionYearAndDegreeCode service =
        new ReadExecutionDegreeByExecutionYearAndDegreeCode();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionDegreeByExecutionYearAndDegreeCode getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadExecutionDegreeByExecutionYearAndDegreeCode";
    }

    public InfoExecutionDegree run(String executionDegreeString, String degreeCode)
        throws FenixServiceException
    {

        InfoExecutionDegree result = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionYear executionYear = new ExecutionYear();
            executionYear.setYear(executionDegreeString);

            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
            ICursoExecucao executionDegree =
                executionDegreeDAO.readByDegreeCodeAndExecutionYear(degreeCode, executionYear);

            if (executionDegree != null)
            {
                result = Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree);
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}
