/*
 * CreateExamNew.java
 *
 * Created on 2003/10/28
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * Service CreateExamNew
 * 
 * @author Ana e Ricardo
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.Season;

public class CreateExamNew implements IServico {

    private static CreateExamNew _servico = new CreateExamNew();

    /**
     * The singleton access method of this class.
     */
    public static CreateExamNew getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private CreateExamNew() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "CreateExamNew";
    }

    public Boolean run(Calendar examDate, Calendar examStartTime, Calendar examEndTime, Season season,
            String[] executionCourseIDArray, String[] scopeIDArray, String[] roomIDArray)
            throws FenixServiceException {

        Boolean result = new Boolean(false);
        if (executionCourseIDArray.length == 0 || scopeIDArray.length == 0) {
            throw new FenixServiceException("No Execution Courses or Scopes");
        }
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentRoomOccupation roomOccupationDAO = sp.getIPersistentRoomOccupation();
            // Exam
            IExam exam = new Exam(examDate, examStartTime, examEndTime, season);
            exam.setAssociatedRoomOccupation(new ArrayList());
            // Writing the new exam to the database
            try {
                IPersistentExam persistentExam = sp.getIPersistentExam();
                persistentExam.simpleLockWrite(exam);
            } catch (ExistingPersistentException ex) {
                throw new ExistingServiceException(ex);
            }
            // Execution Course list
            List executionCourseList = new ArrayList();
            try {
                for (int i = 0; i < executionCourseIDArray.length; i++) {
                    Integer executionCourseID = new Integer(executionCourseIDArray[i]);
                    IExecutionCourse associatedExecutionCourse = (IExecutionCourse) sp
                            .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,
                                    executionCourseID);
                    if (associatedExecutionCourse == null) {
                        throw new FenixServiceException("Unexisting Execution Course");
                    }
                    executionCourseList.add(associatedExecutionCourse);
                }
            } catch (ExcepcaoPersistencia ex) {
                throw new FenixServiceException(ex);
            }
            exam.setAssociatedExecutionCourses(executionCourseList);

            // Scopes
            List scopesList = new ArrayList();
            try {
                for (int i = 0; i < scopeIDArray.length; i++) {
                    Integer scopeID = new Integer(scopeIDArray[i]);
                    ICurricularCourseScope associatedCurricularCourseScope = (ICurricularCourseScope) sp
                            .getIPersistentCurricularCourseScope().readByOID(
                                    CurricularCourseScope.class, scopeID);
                    if (associatedCurricularCourseScope == null) {
                        throw new FenixServiceException("Unexisting Scope");
                    }
                    scopesList.add(associatedCurricularCourseScope);
                }
            } catch (ExcepcaoPersistencia ex) {
                throw new FenixServiceException(ex);
            }
            exam.setAssociatedCurricularCourseScope(scopesList);

            // This is needed for confirming if doesn't exists an exam for that
            // season
            Iterator iterExecutionCourses = executionCourseList.iterator();
            while (iterExecutionCourses.hasNext()) {
                IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();
                for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++) {
                    IExam examAux = (IExam) executionCourse.getAssociatedExams().get(i);
                    if (examAux.getSeason().equals(season)) {
                        // is necessary to confirm if is for the same scope
                        List scopes = examAux.getAssociatedCurricularCourseScope();
                        Iterator iterScopes = scopes.iterator();
                        while (iterScopes.hasNext()) {
                            CurricularCourseScope scope = (CurricularCourseScope) iterScopes.next();
                            if (scopesList.contains(scope)) {
                                throw new ExistingServiceException();
                            }
                        }
                    }
                }
            }

            // Rooms
            List roomsList = new ArrayList();

            IPeriod period = null;
            if (roomIDArray.length != 0) {
                try {
                    period = (IPeriod) sp.getIPersistentPeriod().readByCalendarAndNextPeriod(examDate,
                            examDate, null);
                    if (period == null) {
                        period = new Period(examDate, examDate);
                        period.setRoomOccupations(new ArrayList());
                        sp.getIPersistentPeriod().lockWrite(period);
                    }
                } catch (ExistingPersistentException ex) {
                    throw new ExistingServiceException(ex);
                }
            }
            try {
                List roomOccupationInDBList = sp.getIPersistentRoomOccupation().readAll();

                for (int i = 0; i < roomIDArray.length; i++) {
                    IRoom room = (IRoom) sp.getISalaPersistente().readByOID(Room.class,
                            new Integer(roomIDArray[i]));
                    if (room == null) {
                        throw new FenixServiceException("Unexisting Room");
                    }
                    roomsList.add(room);
                    DiaSemana day = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));

                    RoomOccupation roomOccupation = new RoomOccupation(room, examStartTime, examEndTime,
                            day, RoomOccupation.DIARIA);
                    roomOccupation.setPeriod(period);

                    Iterator iter = roomOccupationInDBList.iterator();
                    while (iter.hasNext()) {
                        IRoomOccupation roomOccupationInDB = (IRoomOccupation) iter.next();
                        if (roomOccupation.roomOccupationForDateAndTime(roomOccupationInDB)) {
                            throw new ExistingServiceException("A sala tá ocupada");
                        }
                    }

                    try {
                        roomOccupationDAO.lockWrite(roomOccupation);
                    } catch (ExistingPersistentException ex) {
                        throw new ExistingServiceException(ex);
                    }
                    exam.getAssociatedRoomOccupation().add(roomOccupation);
                }
            } catch (ExcepcaoPersistencia ex) {
                throw new FenixServiceException(ex);
            }

            result = new Boolean(true);
        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

}