/*
 * Created on 16/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadExecutionCourseTeachers implements IServico
{

    private static ReadExecutionCourseTeachers service = new ReadExecutionCourseTeachers();

    /**
     * The singleton access method of this class.
     */
    public static ReadExecutionCourseTeachers getService()
    {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadExecutionCourseTeachers()
    {
    }

    /**
     * Service name
     */
    public final String getNome()
    {
        return "ReadExecutionCourseTeachers";
    }

    /**
     * Executes the service. Returns the current collection of infoTeachers.
     */

    public List run(Integer executionCourseId) throws FenixServiceException
    {

        List professorShips = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            professorShips =
                sp.getIPersistentExecutionCourse().readExecutionCourseTeachers(executionCourseId);
        } catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (professorShips == null || professorShips.isEmpty())
            return null;

        List infoTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        ITeacher teacher = null;

        while (iter.hasNext())
        {
            teacher = ((IProfessorship) iter.next()).getTeacher();
            infoTeachers.add(Cloner.copyITeacher2InfoTeacher(teacher));
        }

        return infoTeachers;
    }
}