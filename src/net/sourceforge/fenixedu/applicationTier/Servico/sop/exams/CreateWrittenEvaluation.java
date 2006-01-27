package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.util.Season;

public class CreateWrittenEvaluation extends Service {

    /**
     * @param Integer
     *            executionCourseID used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     * @param List
     *            <String> executionCourseIDs used in remaining operations,
     *            allowing more than one execution course to be associated with
     *            the written evaluation
     */
    public void run(Integer executionCourseID, Date writtenEvaluationDate,
            Date writtenEvaluationStartTime, Date writtenEvaluationEndTime,
            List<String> executionCourseIDs, List<String> curricularCourseScopeIDs,
            List<String> roomIDs, Season examSeason, String writtenTestDescription)
            throws FenixServiceException, ExcepcaoPersistencia {

        final List<ExecutionCourse> executionCoursesToAssociate = readExecutionCourses(
                persistentSupport, executionCourseIDs);

        final List<CurricularCourseScope> curricularCourseScopesToAssociate = readCurricularCourseScopes(
                persistentSupport, curricularCourseScopeIDs);

        List<OldRoom> roomsToAssociate = null;
        OccupationPeriod period = null;
        if (roomIDs != null) {
            roomsToAssociate = readRooms(persistentSupport, roomIDs);
            period = readPeriod(persistentSupport, writtenEvaluationDate);
        }

        // creating the new written evaluation, according to the service
        // arguments
        if (examSeason != null) {
            DomainFactory.makeExam(writtenEvaluationDate,
                    writtenEvaluationStartTime, writtenEvaluationEndTime,
                    executionCoursesToAssociate, curricularCourseScopesToAssociate, roomsToAssociate,
                    period, examSeason);
        } else if (writtenTestDescription != null) {
            DomainFactory.makeWrittenTest(writtenEvaluationDate, writtenEvaluationStartTime,
            		writtenEvaluationEndTime, executionCoursesToAssociate,
                    curricularCourseScopesToAssociate, roomsToAssociate, period, writtenTestDescription);
        } else {
            throw new InvalidArgumentsServiceException();
        }
    }

    private List<ExecutionCourse> readExecutionCourses(final ISuportePersistente persistentSupport,
            final List<String> executionCourseIDs) throws ExcepcaoPersistencia, FenixServiceException {

        if (executionCourseIDs.isEmpty()) {
            throw new FenixServiceException("error.invalidExecutionCourse");
        }
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final String executionCourseID : executionCourseIDs) {
            final ExecutionCourse executionCourse = (ExecutionCourse) persistentObject
                    .readByOID(ExecutionCourse.class, Integer.valueOf(executionCourseID));
            if (executionCourse == null) {
                throw new FenixServiceException("error.invalidExecutionCourse");
            }
            result.add(executionCourse);
        }
        return result;
    }

    private List<CurricularCourseScope> readCurricularCourseScopes(
            final ISuportePersistente persistentSupport, final List<String> curricularCourseScopeIDs)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (curricularCourseScopeIDs.isEmpty()) {
            throw new FenixServiceException("error.invalidCurricularCourseScope");
        }
        final List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
        for (final String curricularCourseScopeID : curricularCourseScopeIDs) {
            final CurricularCourseScope curricularCourseScope = (CurricularCourseScope) persistentObject
                    .readByOID(CurricularCourseScope.class, Integer.valueOf(curricularCourseScopeID));
            if (curricularCourseScope == null) {
                throw new FenixServiceException("error.invalidCurricularCourseScope");
            }
            result.add(curricularCourseScope);
        }
        return result;
    }

    private List<OldRoom> readRooms(final ISuportePersistente persistentSupport, final List<String> roomIDs)
            throws ExcepcaoPersistencia, FenixServiceException {

        final List<OldRoom> result = new ArrayList<OldRoom>();
        for (final String roomID : roomIDs) {
            final OldRoom room = (OldRoom) persistentObject.readByOID(OldRoom.class, Integer.valueOf(roomID));
            if (room == null) {
                throw new FenixServiceException("error.noRoom");
            }
            result.add(room);
        }
        return result;
    }

    private OccupationPeriod readPeriod(final ISuportePersistente persistentSupport,
            final Date writtenEvaluationDate) throws ExcepcaoPersistencia {
        final IPersistentPeriod persistentPeriod = persistentSupport.getIPersistentPeriod();
        OccupationPeriod period = (OccupationPeriod) persistentPeriod.readByCalendarAndNextPeriod(writtenEvaluationDate,
                writtenEvaluationDate, null);
        if (period == null) {
            period = DomainFactory.makeOccupationPeriod(writtenEvaluationDate, writtenEvaluationDate);
        }
        return period;
    }

}
