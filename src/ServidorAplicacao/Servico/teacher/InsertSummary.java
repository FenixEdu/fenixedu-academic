/*
 * Created on 21/Jul/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.ListIterator;

import DataBeans.InfoSummary;
import Dominio.IAula;
import Dominio.IProfessorship;
import Dominio.ISala;
import Dominio.ISummary;
import Dominio.ITeacher;
import Dominio.ITurno;
import Dominio.Professorship;
import Dominio.Sala;
import Dominio.Summary;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * @author Susana Fernandes modified by Tânia Pousão 5/Abr/2004 21/Jul/2003
 *         fenix-head ServidorAplicacao.Servico.teacher
 */
public class InsertSummary implements IServico
{

    private static InsertSummary service = new InsertSummary();

    public static InsertSummary getService()
    {

        return service;
    }

    /**
     *  
     */
    public InsertSummary()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome()
    {
        return "InsertSummary";
    }

    public Boolean run(Integer executionCourseId, InfoSummary infoSummary) throws FenixServiceException
    {

        try
        {
            if (infoSummary == null || infoSummary.getInfoShift() == null
                    || infoSummary.getInfoShift().getIdInternal() == null
                    || infoSummary.getIsExtraLesson() == null || infoSummary.getSummaryDate() == null)
            {
                throw new FenixServiceException("error.summary.impossible.insert");
            }

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();        
            ISummary summary = new Summary();
            persistentSummary.simpleLockWrite(summary);
            
            //Shift
            ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();

            ITurno shift = new Turno();
            shift.setIdInternal(infoSummary.getInfoShift().getIdInternal());

            shift = (ITurno) persistentShift.readByOId(shift, false);
            if (shift == null)
            {
                throw new FenixServiceException("error.summary.no.shift");
            }            
            summary.setShift(shift);

            //Extra lesson or not 
            if (infoSummary.getIsExtraLesson().equals(Boolean.TRUE))
            {
                summary.setIsExtraLesson(Boolean.TRUE);

                if (infoSummary.getInfoRoom() != null && infoSummary.getInfoRoom().getIdInternal() != null)
                {
                    ISalaPersistente persistentRoom = persistentSuport.getISalaPersistente();
                    
                    ISala room = new Sala();
                    room.setIdInternal(infoSummary.getInfoRoom().getIdInternal());
                    room = (ISala) persistentRoom.readByOId(room, false);

                    summary.setRoom(room);
                }
            } else
            {
                summary.setIsExtraLesson(Boolean.FALSE);
                
                IAula lesson = findlesson(shift, infoSummary);

                //room
                summary.setRoom(lesson.getSala());

                //validate da summary's date
                infoSummary.setSummaryHour(lesson.getInicio());
                if (!verifyValidDateSummary(shift, infoSummary.getSummaryDate(), infoSummary
                        .getSummaryHour()))
                {
                    throw new FenixServiceException("error.summary.invalid.date");
                }
            }
            
            summary.setSummaryDate(infoSummary.getSummaryDate());
            summary.setSummaryHour(infoSummary.getSummaryHour());      

            //after verify summary date and hour 
            //and before continue check if this summary exists
            if(persistentSummary.readSummaryByUnique(shift, infoSummary.getSummaryDate(), infoSummary.getSummaryHour()) != null) {
                throw new FenixServiceException("error.summary.already.exists");       
            }
            
            summary.setStudentsNumber(infoSummary.getStudentsNumber());

            if (infoSummary.getInfoProfessorship() != null
                    && infoSummary.getInfoProfessorship().getIdInternal() != null)
            {
                System.out.println("professor ship");
                IPersistentProfessorship persistentProfessorship = persistentSuport
                        .getIPersistentProfessorship();

                IProfessorship professorship = new Professorship();
                professorship.setIdInternal(infoSummary.getInfoProfessorship().getIdInternal());

                professorship = (IProfessorship) persistentProfessorship.readByOId(professorship, false);
                if (professorship == null)
                {
                    throw new FenixServiceException("error.summary.no.teacher");
                }

                summary.setProfessorship(professorship);
            } else if (infoSummary.getInfoTeacher() != null
                    && infoSummary.getInfoTeacher().getTeacherNumber() != null)
            {
                IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();

                ITeacher teacher = persistentTeacher.readByNumber(infoSummary.getInfoTeacher()
                        .getTeacherNumber());
                if (teacher == null)
                {
                    throw new FenixServiceException("error.summary.no.teacher");
                }

                summary.setTeacher(teacher);
            } else if (infoSummary.getTeacherName() != null)
            {
                summary.setTeacherName(infoSummary.getTeacherName());
            } else
            {

                throw new FenixServiceException("error.summary.no.teacher");
            }

            summary.setTitle(infoSummary.getTitle());
            summary.setSummaryText(infoSummary.getSummaryText());
            summary.setLastModifiedDate(Calendar.getInstance().getTime());

            return Boolean.TRUE;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
     * @param shift
     * @param infoSummary
     * @return
     */
    private IAula findlesson(ITurno shift, InfoSummary infoSummary)
    {
        if (shift.getAssociatedLessons() != null && shift.getAssociatedLessons().size() > 0)
        {
            ListIterator iterator = shift.getAssociatedLessons().listIterator();
            while (iterator.hasNext())
            {
                IAula lesson = (IAula) iterator.next();
                if (lesson.getIdInternal().equals(infoSummary.getLessonIdSelected()))
                {
                    return lesson;
                }
            }
        }
        
        return null;
    }

    /**
     * @param shift
     * @param summaryDate
     * @param summaryHour
     * @return
     */
    private boolean verifyValidDateSummary(ITurno shift, Calendar summaryDate, Calendar summaryHour)
    {
        Calendar dateAndHourSummary = Calendar.getInstance();
        dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
        dateAndHourSummary.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        dateAndHourSummary.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));
        dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summaryHour.get(Calendar.HOUR_OF_DAY));
        dateAndHourSummary.set(Calendar.MINUTE, summaryHour.get(Calendar.MINUTE));
        dateAndHourSummary.set(Calendar.SECOND, 00);

        Calendar beginLesson = Calendar.getInstance();
        beginLesson.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
        beginLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        beginLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

        Calendar endLesson = Calendar.getInstance();
        endLesson.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
        endLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        endLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

        if (shift.getAssociatedLessons() != null && shift.getAssociatedLessons().size() > 0)
        {
            ListIterator listIterator = shift.getAssociatedLessons().listIterator();
            while (listIterator.hasNext())
            {
                IAula lesson = (IAula) listIterator.next();

                beginLesson.set(Calendar.HOUR_OF_DAY, lesson.getInicio().get(Calendar.HOUR_OF_DAY));
                beginLesson.set(Calendar.MINUTE, lesson.getInicio().get(Calendar.MINUTE));
                beginLesson.set(Calendar.SECOND, 00);

                endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim().get(Calendar.HOUR_OF_DAY));
                endLesson.set(Calendar.MINUTE, lesson.getFim().get(Calendar.MINUTE));
                endLesson.set(Calendar.SECOND, 00);

                if (dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson.getDiaSemana().getDiaSemana()
                        .intValue()
                        && !beginLesson.after(dateAndHourSummary)
                        && !endLesson.before(dateAndHourSummary))
                {
                    return true;
                }
            }
        }

        return false;
    }
}