/*
 * EditExamNew.java
 * 
 * Created on 2003/11/25
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InterceptingRoomsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.Season;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditExamNew implements IService {

    public Boolean run(Calendar examDate, Calendar examStartTime, Calendar examEndTime, Season season,
            String[] executionCourseIDArray, String[] scopeIDArray, String[] roomIDArray, Integer examID)
            throws FenixServiceException {

        Boolean result = new Boolean(false);

        if (executionCourseIDArray.length == 0 || scopeIDArray.length == 0) {
            throw new FenixServiceException("No Execution Courses or Scopes");
        }

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExam persistentExam = sp.getIPersistentExam();
            IPersistentPeriod persistentPeriod = sp.getIPersistentPeriod();
            IPersistentRoomOccupation roomOccupationDAO = sp.getIPersistentRoomOccupation();

            IExam exam = (IExam) persistentExam.readByOID(Exam.class, examID);
            if (exam == null) {
                throw new FenixServiceException("The exam doesnt exist");
            }
            /** ***** VERIFY IF THE EXAM DOES NOT EXIST ******** */
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            List newExecutionCourses = new ArrayList();
            List scopeIDs = Arrays.asList(scopeIDArray);

            for (int i = 0; i < executionCourseIDArray.length; i++) {
                IExecutionCourse executionCourse = findExecutionCourse(exam
                        .getAssociatedExecutionCourses(), new Integer(executionCourseIDArray[i]));
                if (executionCourse == null) {
                    executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                            ExecutionCourse.class, new Integer(executionCourseIDArray[i]));
                    newExecutionCourses.add(executionCourse);
                }

                for (int j = 0; j < executionCourse.getAssociatedExams().size(); j++) {
                    IEvaluation evaluation = executionCourse.getAssociatedEvaluations()
                            .get(j);
                    if (evaluation instanceof IExam) {
                        IExam examAux = (IExam) evaluation;

                        if (examAux.getSeason().equals(season)) {
                            // is necessary to confirm if is for the same scope
                            List scopes = examAux.getAssociatedCurricularCourseScope();
                            Iterator iterScopes = scopes.iterator();
                            while (iterScopes.hasNext()) {
                                ICurricularCourseScope scope = (ICurricularCourseScope) iterScopes.next();
                                if (scopeIDs.contains(scope.getIdInternal().toString())
                                        && !examAux.getIdInternal().equals(exam.getIdInternal())) {
                                    throw new ExistingServiceException();
                                }
                            }
                        }
                    }
                }
            }

            /** ************ */
            persistentExam.simpleLockWrite(exam);

            // Scopes
            for (int i = 0; i < scopeIDArray.length; i++) {
                Integer scopeID = new Integer(scopeIDArray[i]);
                ICurricularCourseScope curricularCourseScope = findScopes(exam
                        .getAssociatedCurricularCourseScope(), scopeID);
                if (curricularCourseScope == null) {
                    curricularCourseScope = (ICurricularCourseScope) sp
                            .getIPersistentCurricularCourseScope().readByOID(
                                    CurricularCourseScope.class, new Integer(scopeIDArray[i]));
                    if (curricularCourseScope == null) {
                        throw new FenixServiceException("The curricular course scope doesnt exist");
                    }
                    exam.getAssociatedCurricularCourseScope().add(curricularCourseScope);
                }
            }

            List indexesToRemove = new ArrayList();

            for (int i = 0; i < exam.getAssociatedCurricularCourseScope().size(); i++) {
                ICurricularCourseScope curricularCourseScope = exam
                        .getAssociatedCurricularCourseScope().get(i);
                if (!scopeIDs.contains(curricularCourseScope.getIdInternal().toString())) {
                    indexesToRemove.add(new Integer(i));
                }
            }
            for (int i = indexesToRemove.size(); i > 0; i--) {
                exam.getAssociatedCurricularCourseScope().remove(
                        ((Integer) indexesToRemove.get(i - 1)).intValue());
            }

            // Execution Courses
            for (int i = 0; i < executionCourseIDArray.length; i++) {
                Integer executionCourseID = new Integer(executionCourseIDArray[i]);
                IExecutionCourse executionCourse = findExecutionCourse(exam
                        .getAssociatedExecutionCourses(), executionCourseID);
                if (executionCourse == null) {
                    executionCourse = findExecutionCourse(newExecutionCourses, executionCourseID);
                    //                        (IExecutionCourse)
                    // sp.getIPersistentExecutionCourse().readByOId(
                    //                            new ExecutionCourse(new
                    // Integer(executionCourseIDArray[i])),
                    //                            false);
                    if (executionCourse == null) {
                        throw new FenixServiceException("The execution course doesnt exist");
                    }
                    exam.getAssociatedExecutionCourses().add(executionCourse);
                }
            }

            List executionCourseIDs = Arrays.asList(executionCourseIDArray);
            for (int i = 0; i < exam.getAssociatedExecutionCourses().size(); i++) {
                IExecutionCourse executionCourse = exam
                        .getAssociatedExecutionCourses().get(i);
                if (!executionCourseIDs.contains(executionCourse.getIdInternal().toString())) {
                    exam.getAssociatedExecutionCourses().remove(executionCourse);
                }
            }
            /** ********************* */

            /** ******************** */
            // Rooms
            List roomsList = new ArrayList();
            List roomOccupationList = new ArrayList();
            IPeriod period;
            if (exam.getAssociatedRoomOccupation() != null
                    && exam.getAssociatedRoomOccupation().size() != 0) {
                period = exam.getAssociatedRoomOccupation().get(0).getPeriod();

                List rooms = period.getRoomOccupations();
                if (rooms.size() == exam.getAssociatedRoomOccupation().size()) {
                    persistentPeriod.simpleLockWrite(period);
                    period.setStartDate(examDate);
                    period.setEndDate(examDate);
                } else {
                    period = (IPeriod) sp.getIPersistentPeriod().readByCalendarAndNextPeriod(examDate,
                            examDate, null);
                    if (period == null) {
                        period = new Period();
                        period.setRoomOccupations(new ArrayList());
                        persistentPeriod.simpleLockWrite(period);
                        period.setStartDate(examDate);
                        period.setEndDate(examDate);
                    }
                }

            } else {

                period = (IPeriod) sp.getIPersistentPeriod().readByCalendarAndNextPeriod(examDate,
                        examDate, null);
                if (period == null) {
                    period = new Period();
                    period.setRoomOccupations(new ArrayList());
                    persistentPeriod.simpleLockWrite(period);
                    period.setStartDate(examDate);
                    period.setEndDate(examDate);
                }
            }

            try {
                List roomOccupationInDBList = sp.getIPersistentRoomOccupation().readAll();
                List roomOccupations = exam.getAssociatedRoomOccupation();
                for (int i = 0; i < roomOccupations.size(); i++) {
                    roomOccupationInDBList.remove(roomOccupations.get(i));
                }

                for (int i = 0; i < roomIDArray.length; i++) {
                    IRoom room = (IRoom) sp.getISalaPersistente().readByOID(Room.class,
                            new Integer(roomIDArray[i]));

                    if (room == null) {
                        throw new FenixServiceException("The room doesnt exist");
                    }

                    roomsList.add(room);
                    DiaSemana weekday = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));

                    IRoomOccupation roomOccupation = new RoomOccupation(room, examStartTime, examEndTime,
                            weekday, RoomOccupation.DIARIA);
                    roomOccupation.setPeriod(period);

                    Iterator iter = roomOccupationInDBList.iterator();
                    while (iter.hasNext()) {
                        IRoomOccupation roomOccupationInDB = (IRoomOccupation) iter.next();

                        if (roomOccupation.roomOccupationForDateAndTime(roomOccupationInDB)) {
                            throw new InterceptingRoomsServiceException(roomOccupation.getRoom()
                                    .getNome());
                        }
                    }

                    if (!exam.getAssociatedRoomOccupation().contains(roomOccupation)) {
                        try {
                            roomOccupationDAO.simpleLockWrite(roomOccupation);
                        } catch (ExistingPersistentException ex) {
                            throw new ExistingServiceException(ex);
                        }
                        exam.getAssociatedRoomOccupation().add(roomOccupation);
                    }

                    roomOccupationList.add(roomOccupation);
                }

                Iterator iterator = exam.getAssociatedRoomOccupation().iterator();
                List roomOcupationToRemove = new ArrayList();
                while (iterator.hasNext()) {
                    IRoomOccupation oldRoomOccupation = (IRoomOccupation) iterator.next();
                    if (!roomOccupationList.contains(oldRoomOccupation)) {
                        roomOcupationToRemove.add(oldRoomOccupation);
                    }
                }
                if (roomOcupationToRemove.size() > 0) {
                    exam.getAssociatedRoomOccupation().removeAll(roomOcupationToRemove);
                }
            } catch (ExcepcaoPersistencia ex) {
                throw new FenixServiceException(ex);
            }
            exam.setDay(examDate);
            exam.setBeginning(examStartTime);
            exam.setEnd(examEndTime);
            exam.setSeason(season);

            result = new Boolean(true);

        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

    /**
     * @param list
     * @param scopeID
     * @return
     */
    private ICurricularCourseScope findScopes(List scopes, final Integer scopeID) {
        return (ICurricularCourseScope) CollectionUtils.find(scopes, new Predicate() {
            public boolean evaluate(Object arg0) {
                if (arg0 != null && arg0 instanceof ICurricularCourseScope) {
                    ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                    return curricularCourseScope.getIdInternal().equals(scopeID);
                }
                return false;
            }
        });
    }

    /**
     * @param list
     * @param executionCourseID
     * @return
     */
    private IExecutionCourse findExecutionCourse(List executionCourses, final Integer executionCourseID) {
        return (IExecutionCourse) CollectionUtils.find(executionCourses, new Predicate() {
            public boolean evaluate(Object arg0) {
                if (arg0 != null && arg0 instanceof IExecutionCourse) {
                    IExecutionCourse executionCourse = (IExecutionCourse) arg0;
                    return executionCourse.getIdInternal().equals(executionCourseID);
                }
                return false;
            }
        });
    }
}