/*
 * ReadExamsWithRooms.java
 *
 * Created on 2003/05/28
 */

package ServidorAplicacao.Servico.student;

/**
 *
 * @author João Mota
 **/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteStudentExamDistributions;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IExamStudentRoom;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamStudentRoom;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExamsWithRooms implements IServico
{

    private static ReadExamsWithRooms _servico = new ReadExamsWithRooms();
    /**
     * The singleton access method of this class.
     **/
    public static ReadExamsWithRooms getService()
    {
        return _servico;
    }

    /**
     * The actor of this class.
     **/
    private ReadExamsWithRooms()
    {
    }

    /**
     * Devolve o nome do servico
     **/
    public final String getNome()
    {
        return "ReadExamsWithRooms";
    }

    public Object run(String username) throws FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();

            IExecutionPeriod currentPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            IStudent student = persistentStudent.readByUsername(username);
            if (student == null)
            {
                throw new InvalidArgumentsServiceException();
            }
            List examsRoomDistribution = persistentExamStudentRoom.readBy(student);
            Iterator iter = examsRoomDistribution.iterator();
            List validDistributions = new ArrayList();
            while (iter.hasNext())
            {
                IExamStudentRoom examStudentRoom = (IExamStudentRoom) iter.next();
                IExecutionCourse executionCourse =
                    (IExecutionCourse) examStudentRoom.getExam().getAssociatedExecutionCourses().get(
                        0);
                if (currentPeriod != null
                    && executionCourse != null
                    && executionCourse.getExecutionPeriod() != null
                    && currentPeriod.equals(executionCourse.getExecutionPeriod()))
                {
                    validDistributions.add(
                        Cloner.copyIExamStudentRoom2InfoExamStudentRoom(examStudentRoom));
                }
            }
            ISiteComponent component = new InfoSiteStudentExamDistributions(validDistributions);
            SiteView siteView = new SiteView(component);
            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

    }
}