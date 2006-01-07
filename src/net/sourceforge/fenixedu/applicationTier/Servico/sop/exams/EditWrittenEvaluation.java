package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.Season;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditWrittenEvaluation implements IService {

    /**
     * @param Integer executionCourseID
     *            used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     * @param List<String> executionCourseIDs
     *            used in remaining operations, allowing more than one execution
     *            course to be associated with the written evaluation
     */
    public void run(Integer executionCourseID, Date writtenEvaluationDate, Date writtenEvaluationStartTime,
            Date writtenEvaluationEndTime, List<String> executionCourseIDs,
            List<String> curricularCourseScopeIDs, List<String> roomIDs, Integer writtenEvaluationOID,
            Season examSeason, String writtenTestDescription) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();

        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) persistentObject.readByOID(
                WrittenEvaluation.class, writtenEvaluationOID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }

        final List<ExecutionCourse> executionCoursesToAssociate = readExecutionCourses(
                persistentSupport, executionCourseIDs);
        final List<CurricularCourseScope> curricularCourseScopeToAssociate = readCurricularCourseScopes(
                persistentSupport, curricularCourseScopeIDs);

        List<Room> roomsToAssociate = null; 
        OccupationPeriod period = null; 
        if (roomIDs != null) {
            roomsToAssociate = readRooms(persistentSupport, roomIDs);
            period = readPeriod(persistentSupport, writtenEvaluation, writtenEvaluationDate); 
        }

        if (examSeason != null) {
            ((Exam) writtenEvaluation).edit(writtenEvaluationDate, writtenEvaluationStartTime, 
            		writtenEvaluationEndTime, executionCoursesToAssociate,
                    curricularCourseScopeToAssociate, roomsToAssociate, period, examSeason);
        } else if (writtenTestDescription != null) {
            ((WrittenTest) writtenEvaluation).edit(writtenEvaluationDate, writtenEvaluationStartTime, 
            		writtenEvaluationEndTime, executionCoursesToAssociate,
            		curricularCourseScopeToAssociate, roomsToAssociate,
                    period, writtenTestDescription);
        } else {
            throw new InvalidArgumentsServiceException();
        }
    }

    private OccupationPeriod readPeriod(final ISuportePersistente persistentSupport,
            final WrittenEvaluation writtenEvaluation, final Date writtenEvaluationDate)
            throws ExcepcaoPersistencia {
        OccupationPeriod period = null;
        if (!writtenEvaluation.getAssociatedRoomOccupation().isEmpty()) {
            period = writtenEvaluation.getAssociatedRoomOccupation().get(0).getPeriod();
            if (writtenEvaluation.getAssociatedRoomOccupation().containsAll(period.getRoomOccupations())) {
                period.setStart(writtenEvaluationDate);
                period.setEnd(writtenEvaluationDate);
            } else {
                period = null;
            }
        }
        if (period == null) {
            final IPersistentPeriod persistentPeriod = persistentSupport.getIPersistentPeriod();
            period = (OccupationPeriod) persistentPeriod.readByCalendarAndNextPeriod(writtenEvaluationDate, writtenEvaluationDate, null);
            if (period == null) {
                period = DomainFactory.makeOccupationPeriod(writtenEvaluationDate, writtenEvaluationDate);
            }
        }
        return period;
    }

    private List<Room> readRooms(final ISuportePersistente persistentSupport, final List<String> roomIDs)
            throws ExcepcaoPersistencia, FenixServiceException {

        final List<Room> result = new ArrayList<Room>();
        final ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();
        for (final String roomID : roomIDs) {
            final Room room = (Room) persistentRoom.readByOID(Room.class, Integer.valueOf(roomID));
            if (room == null) {
                throw new FenixServiceException("error.noRoom");
            }
            result.add(room);
        }
        return result;
    }

    private List<CurricularCourseScope> readCurricularCourseScopes(
            final ISuportePersistente persistentSupport, final List<String> curricularCourseScopeIDs)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (curricularCourseScopeIDs.isEmpty()) {
            throw new FenixServiceException("error.InvalidCurricularCourseScope");
        }
        final List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
        final IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSupport
                .getIPersistentCurricularCourseScope();
        for (final String curricularCourseScopeID : curricularCourseScopeIDs) {
            final CurricularCourseScope curricularCourseScope = (CurricularCourseScope) persistentCurricularCourseScope
                    .readByOID(CurricularCourseScope.class, Integer.valueOf(curricularCourseScopeID));
            if (curricularCourseScope == null) {
                throw new FenixServiceException("error.InvalidCurricularCourseScope");
            }
            result.add(curricularCourseScope);
        }
        return result;
    }

    private List<ExecutionCourse> readExecutionCourses(final ISuportePersistente persistentSupport,
            final List<String> executionCourseIDs) throws ExcepcaoPersistencia, FenixServiceException {

        if (executionCourseIDs.isEmpty()) {
            throw new FenixServiceException("error.invalidExecutionCourse");
        }
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();
        for (final String executionCourseID : executionCourseIDs) {
            final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, Integer.valueOf(executionCourseID));
            if (executionCourse == null) {
                throw new FenixServiceException("error.invalidExecutionCourse");
            }
            result.add(executionCourse);
        }
        return result;
    }

}
