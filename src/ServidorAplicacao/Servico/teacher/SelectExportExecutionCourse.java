package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 */
public class SelectExportExecutionCourse implements IServico
{

    private static SelectExportExecutionCourse _servico = new SelectExportExecutionCourse();

    /**
	 * The actor of this class.
	 */

    private SelectExportExecutionCourse()
    {

    }

    /**
	 * Returns Service Name
	 */
    public String getNome()
    {
        return "SelectExportExecutionCourse";
    }

    /**
	 * Returns the _servico.
	 * 
	 * @return SelectExportExecutionCourse
	 */
    public static SelectExportExecutionCourse getService()
    {
        return _servico;
    }

    public Object run(
        InfoExecutionDegree infoExecutionDegree,
        InfoExecutionPeriod infoExecutionPeriod,
        Integer curricularYear)
    {

        List infoExecutionCourseList = new ArrayList();

        try
        {
            List executionCourseList = null;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            ICursoExecucao executionDegree =
                Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
            IExecutionPeriod executionPeriod =
                Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            executionCourseList =
                executionCourseDAO.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
                    curricularYear,
                    executionPeriod,
                    executionDegree);

            for (int i = 0; i < executionCourseList.size(); i++)
            {
                IExecutionCourse aux = (IExecutionCourse) executionCourseList.get(i);
                InfoExecutionCourse infoExecutionCourse =
                    (InfoExecutionCourse) Cloner.get(aux);
                infoExecutionCourseList.add(infoExecutionCourse);
            }

        }
        catch (ExcepcaoPersistencia e)
        {

            e.printStackTrace();
        }

        return infoExecutionCourseList;

    }

}
