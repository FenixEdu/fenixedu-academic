/*
 * ReadExamsByDayAndBeginningServiceTest.java
 * 
 * Created on 2003/03/19
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExam;
import DataBeans.InfoViewExam;
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.ISala;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseRequeiersAuthorizationServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExamsByDayAndBeginningServiceTest extends TestCaseRequeiersAuthorizationServices
{

    Calendar beginning = null;

    public ReadExamsByDayAndBeginningServiceTest(java.lang.String testName)
    {
        super(testName);
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(ReadExamsByDayAndBeginningServiceTest.class);

        return suite;
    }

    protected void setUp()
    {
        super.setUp();
    }

    protected void tearDown()
    {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadExamsByDayAndBeginning";
    }

    public void testReadValidResult()
    {
        beginning = Calendar.getInstance();
        beginning.set(Calendar.YEAR, 2003);
        beginning.set(Calendar.MONTH, Calendar.JUNE);
        beginning.set(Calendar.DAY_OF_MONTH, 26);
        beginning.set(Calendar.HOUR_OF_DAY, 13);
        beginning.set(Calendar.MINUTE, 0);
        beginning.set(Calendar.SECOND, 0);

        args = new Object[2];
        args[0] = beginning;
        args[1] = beginning;

        try
        {
            callServiceWithAuthorizedUserView();
        }
        catch (FenixServiceException e)
        {
            fail("Unexpected exception: " + e);
        }

        InfoViewExam infoViewExam = new InfoViewExam();
        ArrayList infoViewExams = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            sp.iniciarTransaccao();

            List exams = sp.getIPersistentExam().readBy(beginning, beginning);

            IExam tempExam = null;
            InfoExam tempInfoExam = null;
            List tempAssociatedCurricularCourses = null;
            ICurso tempDegree = null;
            List tempInfoExecutionCourses = null;
            List tempInfoDegrees = null;
            Integer numberStudentesAttendingCourse = null;
            int totalNumberStudents = 0;

            for (int i = 0; i < exams.size(); i++)
            {
                tempExam = (IExam) exams.get(i);
                tempInfoExam = Cloner.copyIExam2InfoExam(tempExam);
                tempInfoDegrees = new ArrayList();
                tempInfoExecutionCourses = new ArrayList();

                for (int k = 0; k < tempExam.getAssociatedExecutionCourses().size(); k++)
                {
                    IExecutionCourse executionCourse =
                        (IExecutionCourse) tempExam.getAssociatedExecutionCourses().get(k);
                    tempInfoExecutionCourses.add(
                        Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));

                    tempAssociatedCurricularCourses = executionCourse.getAssociatedCurricularCourses();
                    for (int j = 0; j < tempAssociatedCurricularCourses.size(); j++)
                    {
                        tempDegree =
                            ((ICurricularCourse) tempAssociatedCurricularCourses.get(j))
                                .getDegreeCurricularPlan()
                                .getDegree();
                        tempInfoDegrees.add(Cloner.copyIDegree2InfoDegree(tempDegree));
                    }

                    numberStudentesAttendingCourse =
                        sp.getIFrequentaPersistente().countStudentsAttendingExecutionCourse(
                            executionCourse);
                    totalNumberStudents += numberStudentesAttendingCourse.intValue();
                }

                infoViewExams.add(
                    new InfoViewExamByDayAndShift(
                        tempInfoExam,
                        tempInfoExecutionCourses,
                        tempInfoDegrees,
                        numberStudentesAttendingCourse));
            }

            infoViewExam.setInfoViewExamsByDayAndShift(infoViewExams);

            List rooms = sp.getISalaPersistente().readAll();
            int totalExamCapacity = 0;
            for (int i = 0; i < rooms.size(); i++)
                totalExamCapacity += ((ISala) rooms.get(i)).getCapacidadeExame().intValue();

            infoViewExam.setAvailableRoomOccupation(
                new Integer(totalExamCapacity - totalNumberStudents));
            sp.confirmarTransaccao();
        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }

        assertEquals("Unexpected result!", infoViewExam, result);
    }

    protected boolean needsAuthorization()
    {
        return true;
    }

}
