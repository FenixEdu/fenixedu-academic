/*
 * Created on 27/Jan/2004
 */
package middleware.credits;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.Credits;
import Dominio.IAula;
import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.IShiftProfessorship;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import Dominio.ITurno;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.credits.IPersistentCredits;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import Util.date.TimePeriod;

/**
 * @author jpvl
 */
public class CreditsInitializer
{
    private static HashMap creditsMap = new HashMap();
    public static void main(String[] args)
    {
        ISuportePersistente sp = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();
            /** dao objects initializations */
            IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();
            IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO =
                sp.getIPersistentTeacherDegreeFinalProjectStudent();
            IPersistentShiftProfessorship shiftProfessorshipDAO = sp.getIPersistentShiftProfessorship();
            IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO =
                sp.getIPersistentTeacherInstitutionWorkingTime();

            sp.iniciarTransaccao();

            IExecutionPeriod executionPeriod =
                (IExecutionPeriod) executionPeriodDAO.readActualExecutionPeriod();
            System.out.println("Calculate support lessons...");
            calculateSupportLessonsCredits(executionPeriod, supportLessonDAO, creditsDAO);

//            sp.confirmarTransaccao();
//            creditsMap.clear();
//            sp.iniciarTransaccao();

            System.out.println("\nCalculate final projects...");
            calculateTeacherDegreeFinalProjectStudentCredits(
                executionPeriod,
                teacherDegreeFinalProjectStudentDAO,
                creditsDAO);

//            sp.confirmarTransaccao();
//            creditsMap.clear();
//            sp.iniciarTransaccao();

            System.out.println("\nCalculate work time...");
            calculateTeacherInstitutionWorkingTime(
                executionPeriod,
                teacherInstitutionWorkingTimeDAO,
                creditsDAO);

//            sp.confirmarTransaccao();
//            creditsMap.clear();
//            sp.iniciarTransaccao();

            System.out.println("\nCalculate lessons...");
            calculateLessonsCredits(executionPeriod, shiftProfessorshipDAO, creditsDAO);

            sp.confirmarTransaccao();
        } catch (Throwable e)
        {
            if (sp != null)
            {
                try
                {
                    sp.cancelarTransaccao();
                } catch (ExcepcaoPersistencia e1)
                {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace(System.out);
        }
    }
    /**
     * @param executionPeriod
     * @param teacherInstitutionWorkingTimeDAO
     */
    private static void calculateTeacherInstitutionWorkingTime(
        IExecutionPeriod executionPeriod,
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO,
        IPersistentCredits creditsDAO)
        throws ExcepcaoPersistencia
    {
        List list = teacherInstitutionWorkingTimeDAO.readByExecutionPeriod(executionPeriod);
        System.out.println("size=" + list.size());
        for (Iterator iter = list.iterator(); iter.hasNext();)
        {
            ITeacherInstitutionWorkTime teacherInstitutionWorkTime =
                (ITeacherInstitutionWorkTime) iter.next();

            ICredits credits =
                getAndLockTeacherCredits(
                    teacherInstitutionWorkTime.getTeacher(),
                    executionPeriod,
                    creditsDAO);

            double actualCredits =
                credits.getInstitutionWorkTime() == null
                    ? 0
                    : credits.getInstitutionWorkTime().doubleValue();

            TimePeriod timePeriod =
                new TimePeriod(
                    teacherInstitutionWorkTime.getStartTime(),
                    teacherInstitutionWorkTime.getEndTime());
            double creditsValue = actualCredits + timePeriod.hours().doubleValue();

            credits.setInstitutionWorkTime(new Double(creditsValue));
        }
    }
    /**
     * @param teacher
     * @param executionPeriod
     * @return
     */
    private static ICredits getAndLockTeacherCredits(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        IPersistentCredits creditsDAO)
        throws ExcepcaoPersistencia
    {
        ICredits credits = (ICredits) creditsMap.get(teacher.getTeacherNumber());
        if (credits == null)
        {
            credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
            if (credits == null)
            {
                credits = new Credits();
                credits.setExecutionPeriod(executionPeriod);
                credits.setTeacher(teacher);

            }

            creditsDAO.simpleLockWrite(credits);

            creditsMap.put(teacher.getTeacherNumber(), credits);
        }
        return credits;
    }
    /**
     * @param executionPeriod
     * @param shiftProfessorshipDAO
     */
    private static void calculateLessonsCredits(
        IExecutionPeriod executionPeriod,
        IPersistentShiftProfessorship shiftProfessorshipDAO,
        IPersistentCredits creditsDAO)
        throws ExcepcaoPersistencia
    {
        List list = shiftProfessorshipDAO.readByExecutionPeriod(executionPeriod);
        System.out.println("size=" + list.size());
        for (Iterator iter = list.iterator(); iter.hasNext();)
        {
            IShiftProfessorship shiftProfessorship = (IShiftProfessorship) iter.next();

            ICredits credits =
                getAndLockTeacherCredits(
                    shiftProfessorship.getProfessorship().getTeacher(),
                    executionPeriod,
                    creditsDAO);

            double actualCredits =
                credits.getLessons() == null ? 0 : credits.getLessons().doubleValue();

            double shiftDuration = calculateShiftDuration(shiftProfessorship.getShift());

            double creditsValue =
                actualCredits + shiftDuration * (shiftProfessorship.getPercentage().doubleValue() / 100);

            credits.setLessons(new Double(creditsValue));
        }
    }
    /**
     * @param turno
     * @return
     */
    private static double calculateShiftDuration(ITurno shift)
    {
        List list = shift.getAssociatedLessons();
        double duration = 0;
        for (Iterator iter = list.iterator(); iter.hasNext();)
        {
            IAula lesson = (IAula) iter.next();

            TimePeriod timePeriod = new TimePeriod(lesson.getInicio(), lesson.getFim());

            duration += timePeriod.hours().doubleValue();

        }
        return duration;
    }
    /**
     * @param executionPeriod
     * @param teacherDegreeFinalProjectStudentDAO
     */
    private static void calculateTeacherDegreeFinalProjectStudentCredits(
        IExecutionPeriod executionPeriod,
        IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO,
        IPersistentCredits creditsDAO)
        throws ExcepcaoPersistencia
    {
        List list = teacherDegreeFinalProjectStudentDAO.readByExecutionPeriod(executionPeriod);
        System.out.println("size=" + list.size());
        for (Iterator iter = list.iterator(); iter.hasNext();)
        {
            ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
                (ITeacherDegreeFinalProjectStudent) iter.next();

            ICredits credits =
                getAndLockTeacherCredits(
                    teacherDegreeFinalProjectStudent.getTeacher(),
                    executionPeriod,
                    creditsDAO);

            double actualCredits =
                credits.getDegreeFinalProjectStudents() == null
                    ? 0
                    : credits.getDegreeFinalProjectStudents().doubleValue();

            double creditsValue =
                actualCredits + teacherDegreeFinalProjectStudent.getPercentage().doubleValue() / 100;

            credits.setDegreeFinalProjectStudents(new Double(creditsValue));
        }
    }
    /**
     * @param executionPeriod
     * @param supportLessonDAO
     */
    private static void calculateSupportLessonsCredits(
        IExecutionPeriod executionPeriod,
        IPersistentSupportLesson supportLessonDAO,
        IPersistentCredits creditsDAO)
        throws ExcepcaoPersistencia
    {
        List list = supportLessonDAO.readByExecutionPeriod(executionPeriod);
        System.out.println("size=" + list.size());
        for (Iterator iter = list.iterator(); iter.hasNext();)
        {
            ISupportLesson supportLesson = (ISupportLesson) iter.next();

            ICredits credits =
                getAndLockTeacherCredits(
                    supportLesson.getProfessorship().getTeacher(),
                    executionPeriod,
                    creditsDAO);

            double actualCredits =
                credits.getSupportLessons() == null ? 0 : credits.getSupportLessons().doubleValue();

            TimePeriod timePeriod =
                new TimePeriod(supportLesson.getStartTime(), supportLesson.getEndTime());

            double creditsValue = actualCredits + timePeriod.hours().doubleValue();

            credits.setSupportLessons(new Double(creditsValue));
        }
    }
}
