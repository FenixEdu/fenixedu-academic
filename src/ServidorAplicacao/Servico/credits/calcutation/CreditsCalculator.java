/*
 * Created on Dec 22, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits.calcutation;

import java.util.Iterator;
import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import Util.date.TimePeriod;

/**
 * @author jpvl
 */
public class CreditsCalculator
{
    private CreditsCalculator calculator = new CreditsCalculator();

    public CreditsCalculator getInstance()
    {
        return calculator;
    }

    /**
     *  
     */
    private CreditsCalculator()
    {
        super();
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param supportLesson
     * @param sp
     * @return @throws
     *              ExcepcaoPersistencia
     */
    public Double calculateSupportLessonsAfterInsert( ITeacher teacher,
                    IExecutionPeriod executionPeriod, ISupportLesson supportLesson,
                    ISuportePersistente sp ) throws ExcepcaoPersistencia
    {
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        List supportLessonList = supportLessonDAO.readByTeacherAndExecutionPeriod(teacher,
                        executionPeriod);

        supportLessonList.remove(supportLesson);

        Iterator iterator = supportLessonList.iterator();

        TimePeriod timePeriod = new TimePeriod(supportLesson.getStartTime(), supportLesson.getEndTime());
        double hours = timePeriod.hours().doubleValue();

        while (iterator.hasNext())
        {
            ISupportLesson sl = (ISupportLesson) iterator.next();
            if (!sl.getIdInternal().equals(supportLesson.getIdInternal()))
            {
                TimePeriod timePeriod2 = new TimePeriod(sl.getStartTime(), sl.getEndTime());
                hours += timePeriod2.hours().doubleValue();
            }
        }

        return new Double(hours + timePeriod.hours().doubleValue());
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param supportLesson
     * @param sp
     * @return @throws
     *              ExcepcaoPersistencia
     */
    public Double calculateSupportLessonsAfterDelete( ITeacher teacher,
                    IExecutionPeriod executionPeriod, ISupportLesson supportLesson,
                    ISuportePersistente sp ) throws ExcepcaoPersistencia
    {
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        List supportLessonList = supportLessonDAO.readByTeacherAndExecutionPeriod(teacher,
                        executionPeriod);

        supportLessonList.remove(supportLesson);

        Iterator iterator = supportLessonList.iterator();
        double hours = 0;
        while (iterator.hasNext())
        {
            ISupportLesson sl = (ISupportLesson) iterator.next();
            if (!sl.getIdInternal().equals(supportLesson.getIdInternal()))
            {
                TimePeriod timePeriod = new TimePeriod(sl.getStartTime(), sl.getEndTime());
                hours += timePeriod.hours().doubleValue();
            }
        }
        return new Double(hours);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param degreeFinalProjectStudent
     * @param sp
     * @return @throws
     *              ExcepcaoPersistencia
     */
    public Double calcuteDegreeFinalProjectStudentAfterInsert( ITeacher teacher,
                    IExecutionPeriod executionPeriod,
                    ITeacherDegreeFinalProjectStudent degreeFinalProjectStudent, ISuportePersistente sp )
                    throws ExcepcaoPersistencia
    {

        IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO = sp
                        .getIPersistentTeacherDegreeFinalProjectStudent();
        List teacherDegreeFinalProjectStudents = teacherDegreeFinalProjectStudentDAO
                        .readByTeacherAndExecutionYear(teacher, executionPeriod.getExecutionYear());

        double numberOfStudents = (double) teacherDegreeFinalProjectStudents.size();
        if (!teacherDegreeFinalProjectStudents.contains(degreeFinalProjectStudent))
        {
            numberOfStudents++;
        }

        return new Double(numberOfStudents);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param degreeFinalProjectStudent
     * @param sp
     * @return @throws
     *              ExcepcaoPersistencia
     */
    public Double calcuteDegreeFinalProjectStudentAfterDelete( ITeacher teacher,
                    IExecutionPeriod executionPeriod,
                    ITeacherDegreeFinalProjectStudent degreeFinalProjectStudent, ISuportePersistente sp )
                    throws ExcepcaoPersistencia
    {
        IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO = sp
                        .getIPersistentTeacherDegreeFinalProjectStudent();
        List teacherDegreeFinalProjectStudents = teacherDegreeFinalProjectStudentDAO
                        .readByTeacherAndExecutionYear(teacher, executionPeriod.getExecutionYear());

        double numberOfStudents = (double) teacherDegreeFinalProjectStudents.size();
        if (teacherDegreeFinalProjectStudents.contains(degreeFinalProjectStudent))
        {
            numberOfStudents--;
        }

        return new Double(numberOfStudents);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param teacherInstitutionWorkTime
     * @param sp
     * @return @throws
     *              ExcepcaoPersistencia
     */
    public Double calcuteTeacherInstitutionWorkingTimeAfterInsert( ITeacher teacher,
                    IExecutionPeriod executionPeriod,
                    ITeacherInstitutionWorkTime teacherInstitutionWorkTime, ISuportePersistente sp )
                    throws ExcepcaoPersistencia
    {
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkTimeDAO = sp
                        .getIPersistentTeacherInstitutionWorkingTime();

        List teacherInstitutionWorkTimes = teacherInstitutionWorkTimeDAO
                        .readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        teacherInstitutionWorkTimes.remove(teacherInstitutionWorkTime);

        Iterator iterator = teacherInstitutionWorkTimes.iterator();
        TimePeriod timePeriod = new TimePeriod(teacherInstitutionWorkTime.getStartTime(),
                        teacherInstitutionWorkTime.getEndTime());
        double hours = timePeriod.hours().doubleValue();
        while (iterator.hasNext())
        {
            ITeacherInstitutionWorkTime tiwt = (ITeacherInstitutionWorkTime) iterator.next();
            if (!tiwt.getIdInternal().equals(teacherInstitutionWorkTime.getIdInternal()))
            {
                TimePeriod timePeriod2 = new TimePeriod(tiwt.getStartTime(), tiwt.getEndTime());
                hours += timePeriod2.hours().doubleValue();
            }
        }

        return new Double(hours);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param teacherInstitutionWorkTime
     * @param sp
     * @return @throws
     *              ExcepcaoPersistencia
     */
    public Double calcuteTeacherInstitutionWorkingTimeAfterDelete( ITeacher teacher,
                    IExecutionPeriod executionPeriod,
                    ITeacherInstitutionWorkTime teacherInstitutionWorkTime, ISuportePersistente sp )
                    throws ExcepcaoPersistencia
    {
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO = sp
                        .getIPersistentTeacherInstitutionWorkingTime();

        List teacherInstitutionWorkTimes = teacherInstitutionWorkingTimeDAO
                        .readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        teacherInstitutionWorkTimes.remove(teacherInstitutionWorkTime);

        Iterator iterator = teacherInstitutionWorkTimes.iterator();
        double hours = 0;
        while (iterator.hasNext())
        {
            ITeacherInstitutionWorkTime tiwt = (ITeacherInstitutionWorkTime) iterator.next();
            if (!tiwt.getIdInternal().equals(teacherInstitutionWorkTime.getIdInternal()))
            {
                TimePeriod timePeriod = new TimePeriod(tiwt.getStartTime(), tiwt.getEndTime());
                hours += timePeriod.hours().doubleValue();
            }
        }

        return new Double(hours);
    }
}
