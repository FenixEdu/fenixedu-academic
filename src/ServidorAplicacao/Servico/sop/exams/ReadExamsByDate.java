/*
 * Created on 19/Mar/2004
 *
 */
package ServidorAplicacao.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 *
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExam;
import DataBeans.InfoViewExam;
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoSala;

public class ReadExamsByDate implements IServico
{

    private static ReadExamsByDate _servico = new ReadExamsByDate();
    /**
     * The singleton access method of this class.
     */
    public static ReadExamsByDate getService()
    {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadExamsByDate()
    {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome()
    {
        return "ReadExamsByDate";
    }

    public InfoViewExam run(Calendar day, Calendar beginning, Calendar end)
    {
        InfoViewExam infoViewExam = new InfoViewExam();
        ArrayList infoViewExams = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            // Read exams on requested day, start and end time
            List exams = sp.getIPersistentExam().readBy(day, beginning, end);

            IExam tempExam = null;
            InfoExam tempInfoExam = null;
            List tempAssociatedCurricularCourses = null;
            ICurso tempDegree = null;
            List tempInfoExecutionCourses = null;
            List tempInfoDegrees = null;
            Integer numberStudentesAttendingCourse = null;
            int totalNumberStudents = 0;

            // For every exam return:
            //    - exam info
            //    - degrees associated with exam
            //    - number of students attending course (potentially attending
            // exam)
            for (int i = 0; i < exams.size(); i++)
            {
                // prepare exam info
                tempExam = (IExam) exams.get(i);
                tempInfoExam = Cloner.copyIExam2InfoExam(tempExam);
                tempInfoDegrees = new ArrayList();
                tempInfoExecutionCourses = new ArrayList();
                int totalNumberStudentsForExam = 0;

                if (tempExam.getAssociatedExecutionCourses() != null)
                {
                    for (int k = 0; k < tempExam.getAssociatedExecutionCourses().size(); k++)
                    {
                        IExecutionCourse executionCourse =
                            (IExecutionCourse) tempExam.getAssociatedExecutionCourses().get(k);
                        tempInfoExecutionCourses.add(Cloner.get(executionCourse));

                        // prepare degrees associated with exam
                        tempAssociatedCurricularCourses =
                            executionCourse.getAssociatedCurricularCourses();
                        for (int j = 0; j < tempAssociatedCurricularCourses.size(); j++)
                        {
                            tempDegree =
                                ((ICurricularCourse) tempAssociatedCurricularCourses.get(j))
                                    .getDegreeCurricularPlan()
                                    .getDegree();
                            tempInfoDegrees.add(Cloner.copyIDegree2InfoDegree(tempDegree));
                        }

                        // determine number of students attending course and
                        // exam
                        numberStudentesAttendingCourse =
                            sp.getIFrequentaPersistente().countStudentsAttendingExecutionCourse(
                                executionCourse);
                        totalNumberStudents += numberStudentesAttendingCourse.intValue();
                        totalNumberStudentsForExam += numberStudentesAttendingCourse.intValue();
                    }
                }

                // add exam and degree info to result list
                infoViewExams.add(
                    new InfoViewExamByDayAndShift(
                        tempInfoExam,
                        tempInfoExecutionCourses,
                        tempInfoDegrees,
                        new Integer(totalNumberStudentsForExam)));
            }

            infoViewExam.setInfoViewExamsByDayAndShift(infoViewExams);

            // Read all rooms to determine total exam capacity
            // TODO : This can be done more efficiently.
            List rooms = sp.getISalaPersistente().readAll();
            int totalExamCapacity = 0;
            for (int i = 0; i < rooms.size(); i++)
            {
                ISala room = (ISala) rooms.get(i);
                if (!room.getTipo().equals(new TipoSala(TipoSala.LABORATORIO)))
                {
                    totalExamCapacity += room.getCapacidadeExame().intValue();
                }
            }

            infoViewExam.setAvailableRoomOccupation(
                new Integer(totalExamCapacity - totalNumberStudents));

        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }

        return infoViewExam;
    }

}
