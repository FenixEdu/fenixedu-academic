/*
 * Created on 19/Mai/2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoProfessorship;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IShiftProfessorship;
import Dominio.ITeacher;
import Dominio.ITurno;
import Dominio.ShiftProfessorship;
import Dominio.Teacher;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.credits.validator.CreditsValidator;
import ServidorAplicacao.Servico.credits.validator.OverlappingLessonPeriod;
import ServidorAplicacao.Servico.credits.validator.OverlappingPeriodException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class AcceptTeacherExecutionCourseShiftPercentage implements IServico
{
    /**
	 * @author jpvl
	 */
    public class InvalidProfessorshipPercentage extends FenixServiceException
    {

    }
    private static AcceptTeacherExecutionCourseShiftPercentage service = new AcceptTeacherExecutionCourseShiftPercentage();

    /**
	 * The singleton access method of this class.
	 */
    public static AcceptTeacherExecutionCourseShiftPercentage getService()
    {
        return service;
    }

    private ITurno getIShift(ITurnoPersistente shiftDAO, InfoShiftProfessorship infoShiftProfessorship)
            throws ExcepcaoPersistencia
    {
        ITurno shift = new Turno(infoShiftProfessorship.getInfoShift().getIdInternal());
        shift = (ITurno) shiftDAO.readByOId(shift, false);
        return shift;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "AcceptTeacherExecutionCourseShiftPercentage";
    }

    /**
	 * @param infoTeacherShiftPercentageList
	 *                   list of shifts and percentages that teacher needs...
	 * @return @throws
	 *              FenixServiceException
	 */
    public List run(InfoTeacher infoTeacher, InfoExecutionCourse infoExecutionCourse,
            List infoShiftProfessorshipList) throws FenixServiceException
    {
        List shiftWithErrors = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IPersistentShiftProfessorship shiftProfessorshipDAO = sp
                    .getIPersistentTeacherShiftPercentage();
            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

            //read execution course
            IExecutionCourse executionCourse = new ExecutionCourse(infoExecutionCourse.getIdInternal());
            executionCourse = (IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);

            //read teacher
            ITeacher teacher = new Teacher(infoTeacher.getIdInternal());
            teacher = (ITeacher) teacherDAO.readByOId(teacher, false);

            //read professorship
            IProfessorship professorship = professorshipDAO.readByTeacherIDAndExecutionCourseID(teacher,
                    executionCourse);

            if (professorship != null)
            {
                Iterator iterator = infoShiftProfessorshipList.iterator();
                List shiftProfessorshipAdded = addShiftProfessorships(shiftDAO, shiftProfessorshipDAO,
                        professorship, iterator);
                validateShiftProfessorshipAdded(shiftProfessorshipAdded);
            }
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException(e);
        }

        return shiftWithErrors; //retorna a lista com os turnos que causaram erros!
    }

    private List addShiftProfessorships(ITurnoPersistente shiftDAO,
            IPersistentShiftProfessorship shiftProfessorshipDAO, IProfessorship professorship,
            Iterator iterator)
            throws InvalidProfessorshipPercentage, ExcepcaoPersistencia, OverlappingPeriodException, 
            FenixServiceException
    {
        List shiftProfessorshipAdded = new ArrayList();
        while (iterator.hasNext())
        {
            InfoShiftProfessorship infoShiftProfessorship = (InfoShiftProfessorship) iterator.next();

            Double percentage = infoShiftProfessorship.getPercentage();
            if ((percentage != null)
                    && ((percentage.doubleValue() > 100) || (percentage.doubleValue() < 0)))
            {
                throw new InvalidProfessorshipPercentage();
            }

            ITurno shift = getIShift(shiftDAO, infoShiftProfessorship);

            IShiftProfessorship shiftProfessorship = shiftProfessorshipDAO.readByProfessorshipAndShift(
                    professorship, shift);

            shiftProfessorship = lockOrDeleteShiftProfessorship(shiftProfessorshipDAO, professorship,
                    percentage, shift, shiftProfessorship);

            if (percentage.doubleValue() != 0)
            {
                shiftProfessorshipAdded.add(shiftProfessorship);
            }

        }
        return shiftProfessorshipAdded;
    }

    private IShiftProfessorship lockOrDeleteShiftProfessorship(
            IPersistentShiftProfessorship shiftProfessorshipDAO, IProfessorship professorship,
            Double percentage, ITurno shift, IShiftProfessorship shiftProfessorship)
            throws ExcepcaoPersistencia, OverlappingPeriodException, FenixServiceException
    {
        if (percentage.doubleValue() == 0)
        {
            shiftProfessorshipDAO.delete(shiftProfessorship);
        }
        else
        {

            if (shiftProfessorship == null)
            {
                shiftProfessorship = new ShiftProfessorship();
                shiftProfessorship.setProfessorship(professorship);
                shiftProfessorship.setShift(shift);
            }
            shiftProfessorshipDAO.simpleLockWrite(shiftProfessorship);

            shiftProfessorship.setPercentage(percentage);

            CreditsValidator.validatePeriod(professorship.getTeacher(), professorship
                    .getExecutionCourse().getExecutionPeriod(),
                    shiftProfessorship);
        }
        return shiftProfessorship;
    }

    /**
	 * @param infoShiftProfessorshipAdded
	 */
    private void validateShiftProfessorshipAdded(List shiftProfessorshipAdded)
            throws OverlappingLessonPeriod
    {

        if (shiftProfessorshipAdded.size() > 1)
        {
            ArrayList lessonsList = new ArrayList();
            ArrayList fullShiftLessonList = new ArrayList();
            for (int i = 0; i < shiftProfessorshipAdded.size(); i++)
            {
                final IShiftProfessorship shiftProfessorship = (IShiftProfessorship) shiftProfessorshipAdded
                        .get(i);
                List shiftLessons = shiftProfessorship.getShift().getAssociatedLessons();
                lessonsList.addAll(shiftLessons);
                if (shiftProfessorship.getPercentage().doubleValue() == 100)
                {
                    fullShiftLessonList.addAll(shiftLessons);
                }
            }

            for (int j = 0; j < fullShiftLessonList.size(); j++)
            {
                IAula lesson = (IAula) lessonsList.get(j);
                if (overlapsWithAny(lesson, lessonsList))
                {
                    throw new OverlappingLessonPeriod();
                }
            }
        }
    }

    /**
	 * @param lesson
	 * @param lessonsList
	 * @return
	 */
    private boolean overlapsWithAny(IAula lesson, ArrayList lessonsList)
    {
        DiaSemana lessonWeekDay = lesson.getDiaSemana();
        Calendar lessonStart = lesson.getInicio();
        Calendar lessonEnd = lesson.getFim();
        for (int i = 0; i < lessonsList.size(); i++)
        {
            IAula otherLesson = (IAula) lessonsList.get(i);
            if (!otherLesson.equals(lesson))
            {
                if (otherLesson.getDiaSemana().equals(lessonWeekDay))
                {
                    Calendar otherStart = otherLesson.getInicio();
                    Calendar otherEnd = otherLesson.getFim();
                    if (((otherStart.equals(lessonStart)) && otherEnd.equals(lessonEnd))
                            || (lessonStart.before(otherEnd) && lessonStart.after(otherStart))
                            || (lessonEnd.before(otherEnd) && lessonEnd.after(otherStart)))
                    {
                        return true;
                    }

                }
            }
        }
        return false;
    }
}