/*
 * EditExamNew.java
 * 
 * Created on 2003/11/25
 */

package ServidorAplicacao.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.CurricularCourseScope;
import Dominio.Exam;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEvaluation;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IPeriod;
import Dominio.IRoomOccupation;
import Dominio.ISala;
import Dominio.Period;
import Dominio.RoomOccupation;
import Dominio.Sala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentPeriod;
import ServidorPersistente.IPersistentRoomOccupation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DiaSemana;
import Util.Season;

public class EditExamNew implements IServico
{

    private static EditExamNew _servico = new EditExamNew();
    /**
     * The singleton access method of this class.
     */
    public static EditExamNew getService()
    {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private EditExamNew()
    {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome()
    {
        return "EditExamNew";
    }

    public Boolean run(
        Calendar examDate,
        Calendar examStartTime,
        Calendar examEndTime,
        Season season,
        String[] executionCourseIDArray,
        String[] scopeIDArray,
        String[] roomIDArray,
        Integer examID)
        throws FenixServiceException
    {

        Boolean result = new Boolean(false);

        if (executionCourseIDArray.length == 0 || scopeIDArray.length == 0)
        {
            throw new FenixServiceException("No Execution Courses or Scopes");
        }

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExam persistentExam = sp.getIPersistentExam();
            IPersistentPeriod persistentPeriod = sp.getIPersistentPeriod();
            IPersistentRoomOccupation roomOccupationDAO = sp.getIPersistentRoomOccupation();

            IExam exam = (IExam) persistentExam.readByOID(Exam.class, examID);
            if (exam == null)
            {
                throw new FenixServiceException("The exam doesnt exist");
            }

            
            // This is needed for confirming if doesn't exists an exam for that
            // season
            Iterator iterExecutionCourses = exam.getAssociatedExecutionCourses().iterator();
            while (iterExecutionCourses.hasNext())
            {
                IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();
                for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++)
                {
                    IEvaluation evaluation =
                        (IEvaluation) executionCourse.getAssociatedEvaluations().get(i);
                    if (evaluation instanceof IExam)
                    {
                        IExam examAux = (IExam) evaluation;
                        if (examAux.getSeason().equals(season))
                        {
                            // is necessary to confirm if is for the same scope
                            List scopes = examAux.getAssociatedCurricularCourseScope();
                            Iterator iterScopes = scopes.iterator();
                            while (iterScopes.hasNext())
                            {
                                CurricularCourseScope scope = (CurricularCourseScope) iterScopes.next();
                                if (exam.getAssociatedCurricularCourseScope().contains(scope)
                                    && !examAux.getIdInternal().equals(exam.getIdInternal()))
                                {
                                    throw new ExistingServiceException();
                                }
                            }
                        }
                    }
                }
            }

            persistentExam.simpleLockWrite(exam);

            // Scopes
            for (int i = 0; i < scopeIDArray.length; i++)
            {
                Integer scopeID = new Integer(scopeIDArray[i]);
                ICurricularCourseScope curricularCourseScope =
                    findScopes(exam.getAssociatedCurricularCourseScope(), scopeID);
                if (curricularCourseScope == null)
                {
                    curricularCourseScope =
                        (ICurricularCourseScope) sp.getIPersistentCurricularCourseScope().readByOID(
                            CurricularCourseScope.class,
                            new Integer(scopeIDArray[i]));
                    if (curricularCourseScope == null)
                    {
                        throw new FenixServiceException("The curricular course scope doesnt exist");
                    }
                    exam.getAssociatedCurricularCourseScope().add(curricularCourseScope);
                }
            }
            List scopeIDs = Arrays.asList(scopeIDArray);
            List indexesToRemove = new ArrayList();

            for (int i = 0; i < exam.getAssociatedCurricularCourseScope().size(); i++)
            {
                ICurricularCourseScope curricularCourseScope =
                    (ICurricularCourseScope) exam.getAssociatedCurricularCourseScope().get(i);
                if (!scopeIDs.contains(curricularCourseScope.getIdInternal().toString()))
                {
                    indexesToRemove.add(new Integer(i));
                }
            }
            for (int i = indexesToRemove.size(); i > 0; i--)
            {
                exam.getAssociatedCurricularCourseScope().remove(
                    ((Integer) indexesToRemove.get(i - 1)).intValue());
            }

            // Execution Courses
            for (int i = 0; i < executionCourseIDArray.length; i++)
            {
                Integer executionCourseID = new Integer(executionCourseIDArray[i]);
                IExecutionCourse executionCourse =
                    findExecutionCourse(exam.getAssociatedExecutionCourses(), executionCourseID);
                if (executionCourse == null)
                {
                    executionCourse =
                        (IExecutionCourse) sp.getIPersistentExecutionCourse().readByOId(
                            new ExecutionCourse(new Integer(executionCourseIDArray[i])),
                            false);
                    if (executionCourse == null)
                    {
                        throw new FenixServiceException("The execution course doesnt exist");
                    }
                    exam.getAssociatedExecutionCourses().add(executionCourse);
                }
            }

            List executionCourseIDs = Arrays.asList(executionCourseIDArray);
            for (int i = 0; i < exam.getAssociatedExecutionCourses().size(); i++)
            {
                IExecutionCourse executionCourse =
                    (IExecutionCourse) exam.getAssociatedExecutionCourses().get(i);
                if (!executionCourseIDs.contains(executionCourse.getIdInternal().toString()))
                {
                    exam.getAssociatedExecutionCourses().remove(executionCourse);
                }
            }

            // Rooms
            List roomsList = new ArrayList();
            List roomOccupationList = new ArrayList();
            IPeriod period;
            if (roomIDArray.length != 0)
            {
                if (exam.getAssociatedRoomOccupation() != null
                    && exam.getAssociatedRoomOccupation().size() != 0)
                {
                    period = ((IRoomOccupation) exam.getAssociatedRoomOccupation().get(0)).getPeriod();

///
					List rooms = period.getRoomOccupations();
//					if (rooms == null)
//						System.out.println("##### ROOMS NULL");
                    if (rooms.size() == exam.getAssociatedRoomOccupation().size())
                    {
                        persistentPeriod.simpleLockWrite(period);
                        period.setStartDate(examDate);
                        period.setEndDate(examDate);
                    }
                    else
                    {
                        period = (Period) sp.getIPersistentPeriod().readBy(examDate, examDate);
                        if (period == null)
                        {
                            period = new Period();
                            period.setRoomOccupations(new ArrayList());
                            persistentPeriod.simpleLockWrite(period);
                            period.setStartDate(examDate);
                            period.setEndDate(examDate);
                        }
                    }

                }
                else
                {

                    period = (Period) sp.getIPersistentPeriod().readBy(examDate, examDate);
                    if (period == null)
                    {
                        period = new Period();
                        period.setRoomOccupations(new ArrayList());
                        persistentPeriod.simpleLockWrite(period);
                        period.setStartDate(examDate);
                        period.setEndDate(examDate);
                    }
                }

                try
                {
                    List roomOccupationInDBList = sp.getIPersistentRoomOccupation().readAll();
                    List roomOccupations = exam.getAssociatedRoomOccupation();
                    for (int i = 0; i < roomOccupations.size(); i++)
                    {
                    	roomOccupationInDBList.remove(roomOccupations.get(i));
                    }

                    for (int i = 0; i < roomIDArray.length; i++)
                    {
                        ISala room =
                            (ISala) sp.getISalaPersistente().readByOID(
                                Sala.class,
                                new Integer(roomIDArray[i]));

                        if (room == null)
                        {
                            throw new FenixServiceException("The room doesnt exist");
                        }

                        roomsList.add(room);
                        DiaSemana weekday = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));

                        RoomOccupation roomOccupation =
                            new RoomOccupation(room, examStartTime, examEndTime, weekday);
                        roomOccupation.setPeriod(period);

                        Iterator iter = roomOccupationInDBList.iterator();
                        while (iter.hasNext())
                        {
                            IRoomOccupation roomOccupationInDB = (IRoomOccupation) iter.next();

                            if (roomOccupation.roomOccupationForDateAndTime(roomOccupationInDB))
                            {
                                throw new FenixServiceException("A sala tá ocupada");
                            }
                        }

                        if (!exam.getAssociatedRoomOccupation().contains(roomOccupation))
                        {
                            try
                            {
                                roomOccupationDAO.simpleLockWrite(roomOccupation);
                            }
                            catch (ExistingPersistentException ex)
                            {
                                throw new ExistingServiceException(ex);
                            }
                            exam.getAssociatedRoomOccupation().add(roomOccupation);
                        }

                        roomOccupationList.add(roomOccupation);
                    }

                    Iterator iterator = exam.getAssociatedRoomOccupation().iterator();
                    List roomOcupationToRemove = new ArrayList();
                    while (iterator.hasNext())
                    {
                        RoomOccupation oldRoomOccupation = (RoomOccupation) iterator.next();
                        if (!roomOccupationList.contains(oldRoomOccupation))
                        {
                            roomOcupationToRemove.add(oldRoomOccupation);
                        }
                    }
                    if (roomOcupationToRemove.size() > 0)
                    {
                        exam.getAssociatedRoomOccupation().removeAll(roomOcupationToRemove);
                    }
                }
                catch (ExcepcaoPersistencia ex)
                {
                    throw new FenixServiceException(ex);
                }
            }

            exam.setDay(examDate);
            exam.setBeginning(examStartTime);
            exam.setEnd(examEndTime);
            exam.setSeason(season);

            result = new Boolean(true);

        }
        catch (ExcepcaoPersistencia ex)
        {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

    /**
     * @param list
     * @param scopeID
     * @return
     */
    private ICurricularCourseScope findScopes(List scopes, final Integer scopeID)
    {
        return (ICurricularCourseScope) CollectionUtils.find(scopes, new Predicate()
        {
            public boolean evaluate(Object arg0)
            {
                if (arg0 != null && arg0 instanceof ICurricularCourseScope)
                {
                    ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                    if (curricularCourseScope.getIdInternal().equals(scopeID))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        });
    }

    /**
     * @param list
     * @param executionCourseID
     * @return
     */
    private IExecutionCourse findExecutionCourse(List executionCourses, final Integer executionCourseID)
    {
        return (IExecutionCourse) CollectionUtils.find(executionCourses, new Predicate()
        {
            public boolean evaluate(Object arg0)
            {
                if (arg0 != null && arg0 instanceof IExecutionCourse)
                {
                    IExecutionCourse executionCourse = (IExecutionCourse) arg0;
                    if (executionCourse.getIdInternal().equals(executionCourseID))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        });
    }
}