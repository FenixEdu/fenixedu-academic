package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByDCPID implements IServico
{

    private static ReadExecutionDegreeByDCPID service = new ReadExecutionDegreeByDCPID();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionDegreeByDCPID getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadExecutionDegreeByDCPID";
    }

    /**
	 * @param name
	 * @param infoExecutionYear
	 * @return InfoExecutionDegree
	 * @throws FenixServiceException
	 *             This method assumes thar there's only one Execution Degree
	 *             for each Degree Curricular Plan. This is the case with the
	 *             Master Degrees
	 */
    public InfoExecutionDegree run(Integer degreeCurricularPlanID) throws FenixServiceException
    {

        ICursoExecucao executionDegree = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            executionDegree =
                sp.getICursoExecucaoPersistente().readbyDegreeCurricularPlanID(degreeCurricularPlanID);

        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        if (executionDegree == null)
        {
            throw new NonExistingServiceException();
        }

        return (InfoExecutionDegree) Cloner.get(executionDegree);
    }
}
