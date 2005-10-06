package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
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

    public void run(Calendar writtenEvaluationDate, Calendar writtenEvaluationStartTime,
            Calendar writtenEvaluationEndTime, List<String> executionCourseIDs,
            List<String> curricularCourseScopeIDs, List<String> roomIDs, Integer writtenEvaluationOID,
            Season examSeason, String writtenTestDescription) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();

        final IWrittenEvaluation writtenEvaluation = (IWrittenEvaluation) persistentObject.readByOID(
                WrittenEvaluation.class, writtenEvaluationOID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }

        final List<IExecutionCourse> executionCoursesToAssociate = readExecutionCourses(
                persistentSupport, executionCourseIDs);
        final List<ICurricularCourseScope> curricularCourseScopeToAssociate = readCurricularCourseScopes(
                persistentSupport, curricularCourseScopeIDs);

        List<IRoom> roomsToAssociate = null; 
        IPeriod period = null; 
        if (roomIDs != null) {
            roomsToAssociate = readRooms(persistentSupport, roomIDs);
            period = readPeriod(persistentSupport, writtenEvaluation, writtenEvaluationDate); 
        }

        if (examSeason != null) {
            ((IExam) writtenEvaluation).edit(writtenEvaluationDate.getTime(), writtenEvaluationStartTime
                    .getTime(), writtenEvaluationEndTime.getTime(), executionCoursesToAssociate,
                    curricularCourseScopeToAssociate, roomsToAssociate, period, examSeason);
        } else if (writtenTestDescription != null) {
            ((IWrittenTest) writtenEvaluation).edit(writtenEvaluationDate.getTime(),
                    writtenEvaluationStartTime.getTime(), writtenEvaluationEndTime.getTime(),
                    executionCoursesToAssociate, curricularCourseScopeToAssociate, roomsToAssociate,
                    period, writtenTestDescription);
        } else {
            throw new InvalidArgumentsServiceException();
        }
    }

    private IPeriod readPeriod(final ISuportePersistente persistentSupport,
            final IWrittenEvaluation writtenEvaluation, final Calendar writtenEvaluationDate)
            throws ExcepcaoPersistencia {
        IPeriod period = null;
        if (!writtenEvaluation.getAssociatedRoomOccupation().isEmpty()) {
            period = writtenEvaluation.getAssociatedRoomOccupation().get(0).getPeriod();
            if (writtenEvaluation.getAssociatedRoomOccupation().containsAll(period.getRoomOccupations())) {
                period.setStartDate(writtenEvaluationDate);
                period.setEndDate(writtenEvaluationDate);
            } else {
                period = null;
            }
        }
        if (period == null) {
            final IPersistentPeriod persistentPeriod = persistentSupport.getIPersistentPeriod();
            period = (IPeriod) persistentPeriod.readByCalendarAndNextPeriod(writtenEvaluationDate, writtenEvaluationDate, null);
            if (period == null) {
                period = DomainFactory.makePeriod(writtenEvaluationDate.getTime(), writtenEvaluationDate.getTime());
            }
        }
        return period;
    }

    private List<IRoom> readRooms(final ISuportePersistente persistentSupport, final List<String> roomIDs)
            throws ExcepcaoPersistencia, FenixServiceException {

        final List<IRoom> result = new ArrayList<IRoom>();
        final ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();
        for (final String roomID : roomIDs) {
            final IRoom room = (IRoom) persistentRoom.readByOID(Room.class, Integer.valueOf(roomID));
            if (room == null) {
                throw new FenixServiceException("error.noRoom");
            }
            result.add(room);
        }
        return result;
    }

    private List<ICurricularCourseScope> readCurricularCourseScopes(
            final ISuportePersistente persistentSupport, final List<String> curricularCourseScopeIDs)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (curricularCourseScopeIDs.isEmpty()) {
            throw new FenixServiceException("error.InvalidCurricularCourseScope");
        }
        final List<ICurricularCourseScope> result = new ArrayList<ICurricularCourseScope>();
        final IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSupport
                .getIPersistentCurricularCourseScope();
        for (final String curricularCourseScopeID : curricularCourseScopeIDs) {
            final ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope
                    .readByOID(CurricularCourseScope.class, Integer.valueOf(curricularCourseScopeID));
            if (curricularCourseScope == null) {
                throw new FenixServiceException("error.InvalidCurricularCourseScope");
            }
            result.add(curricularCourseScope);
        }
        return result;
    }

    private List<IExecutionCourse> readExecutionCourses(final ISuportePersistente persistentSupport,
            final List<String> executionCourseIDs) throws ExcepcaoPersistencia, FenixServiceException {

        if (executionCourseIDs.isEmpty()) {
            throw new FenixServiceException("error.InvalidExecutionCourse");
        }
        final List<IExecutionCourse> result = new ArrayList<IExecutionCourse>();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();
        for (final String executionCourseID : executionCourseIDs) {
            final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, Integer.valueOf(executionCourseID));
            if (executionCourse == null) {
                throw new FenixServiceException("error.InvalidExecutionCourse");
            }
            result.add(executionCourse);
        }
        return result;
    }

}
