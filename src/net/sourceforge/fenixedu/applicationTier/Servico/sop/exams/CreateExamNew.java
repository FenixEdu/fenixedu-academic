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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.Season;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateExamNew implements IService {

    public Boolean run(Calendar examDate, Calendar examStartTime, Calendar examEndTime, Season season,
            String[] executionCourseIDArray, String[] scopeIDArray, String[] roomIDArray)
            throws FenixServiceException {

        Boolean result = new Boolean(false);
        if (executionCourseIDArray.length == 0 || scopeIDArray.length == 0) {
            throw new FenixServiceException("No Execution Courses or Scopes");
        }
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentRoomOccupation roomOccupationDAO = sp.getIPersistentRoomOccupation();
            // Exam
            IExam exam = DomainFactory.makeExam(examDate, examStartTime, examEndTime, season);
            // Writing the new exam to the database

            // Execution Course list
            List<IExecutionCourse> executionCourseList = new ArrayList<IExecutionCourse>();
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
            exam.getAssociatedExecutionCourses().addAll(executionCourseList);

            // Scopes
            List<ICurricularCourseScope> scopesList = new ArrayList<ICurricularCourseScope>();
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
            exam.getAssociatedCurricularCourseScope().addAll(scopesList);

            // This is needed for confirming if doesn't exists an exam for that
            // season
            Iterator iterExecutionCourses = executionCourseList.iterator();
            while (iterExecutionCourses.hasNext()) {
                IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();
                List<IExam> associatedExams = new ArrayList();
                List<IEvaluation> associatedEvaluations = executionCourse.getAssociatedEvaluations();
                for (IEvaluation evaluation : associatedEvaluations) {
                    if (evaluation instanceof Exam && !evaluation.equals(exam)) {
                        associatedExams.add((IExam) evaluation);
                    }
                }
                for (int i = 0; i < associatedExams.size(); i++) {
                    IExam examAux = associatedExams.get(i);
                    if (examAux.getSeason().equals(season)) {
                        // is necessary to confirm if is for the same scope
                        List scopes = examAux.getAssociatedCurricularCourseScope();
                        Iterator iterScopes = scopes.iterator();
                        while (iterScopes.hasNext()) {
                            ICurricularCourseScope scope = (ICurricularCourseScope) iterScopes.next();
                            if (scopesList.contains(scope)) {
                                throw new ExistingServiceException();
                            }
                        }
                    }
                }
            }

            // Rooms
            IPeriod period = null;
            if (roomIDArray.length != 0) {
                try {
                    period = (IPeriod) sp.getIPersistentPeriod().readByCalendarAndNextPeriod(examDate,
                            examDate, null);
                    if (period == null) {
                        period = DomainFactory.makePeriod(examDate, examDate);
                    }
                } catch (ExistingPersistentException ex) {
                    throw new ExistingServiceException(ex);
                }
            }

            for (int i = 0; i < roomIDArray.length; i++) {
                IRoom room = (IRoom) sp.getISalaPersistente().readByOID(Room.class,
                        new Integer(roomIDArray[i]));
                if (room == null) {
                    throw new FenixServiceException("Unexisting Room");
                }
                DiaSemana day = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));

                room.createRoomOccupation(period, examStartTime, examEndTime, day, RoomOccupation.DIARIA, null, exam);
                
            }

            result = new Boolean(true);
        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

}
