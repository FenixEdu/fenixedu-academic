package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionDegree executionDegree =
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
