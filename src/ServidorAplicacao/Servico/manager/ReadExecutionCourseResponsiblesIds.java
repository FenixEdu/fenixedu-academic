/*
 * Created on 23/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IResponsibleFor;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadExecutionCourseResponsiblesIds implements IServico
{

    private static ReadExecutionCourseResponsiblesIds service = new ReadExecutionCourseResponsiblesIds();

    /**
     * The singleton access method of this class.
     */
    public static ReadExecutionCourseResponsiblesIds getService()
    {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadExecutionCourseResponsiblesIds()
    {
    }

    /**
     * Service name
     */
    public final String getNome()
    {
        return "ReadExecutionCourseResponsiblesIds";
    }

    /**
     * Executes the service. Returns the current collection of ids of teachers.
     */

    public List run(Integer executionCourseId) throws FenixServiceException
    {

        List responsibles = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse executionCourse =
                (IExecutionCourse) sp.getIPersistentExecutionCourse().readByOId(
                    new ExecutionCourse(executionCourseId),
                    false);
            responsibles = sp.getIPersistentResponsibleFor().readByExecutionCourse(executionCourse);
        } catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (responsibles == null || responsibles.isEmpty())
            return new ArrayList();

        List ids = new ArrayList();
        Iterator iter = responsibles.iterator();

        while (iter.hasNext())
        {
            ids.add(((IResponsibleFor) iter.next()).getTeacher().getIdInternal());
        }

        return ids;
    }
}