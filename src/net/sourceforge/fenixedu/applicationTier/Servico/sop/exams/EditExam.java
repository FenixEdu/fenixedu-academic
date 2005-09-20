package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.Season;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditExam implements IService {

    public void run(Calendar examDate, Calendar examStartTime, Calendar examEndTime, Season season,
            List<String> executionCourseIDs, List<String> curricularCourseScopeIDs,
            List<String> roomIDs, Integer examID) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExam persistentExam = persistentSupport.getIPersistentExam();

        final IExam exam = (IExam) persistentExam.readByOID(Exam.class, examID);
        if (exam == null) {
            throw new FenixServiceException("error.noExam");
        }

        final List<IExecutionCourse> executionCoursesToAssociate = readExecutionCourses(
                persistentSupport, executionCourseIDs);
        final List<ICurricularCourseScope> curricularCourseScopeToAssociate = readCurricularCourseScopes(
                persistentSupport, curricularCourseScopeIDs);
        final List<IRoom> roomsToAssociate = readRooms(persistentSupport, roomIDs);
        final IPeriod period = readPeriod(persistentSupport, exam, examDate);

        exam.edit(examDate.getTime(), examStartTime.getTime(), examEndTime.getTime(), season,
                executionCoursesToAssociate, curricularCourseScopeToAssociate, roomsToAssociate, period);
    }

    private IPeriod readPeriod(final ISuportePersistente persistentSupport, final IExam exam,
            final Calendar examDate) throws ExcepcaoPersistencia {
        IPeriod period = null;
        if (!exam.getAssociatedRoomOccupation().isEmpty()) {
            period = exam.getAssociatedRoomOccupation().get(0).getPeriod();
            if (exam.getAssociatedRoomOccupation().containsAll(period.getRoomOccupations())) {
                period.setStartDate(examDate);
                period.setEndDate(examDate);
            } else {
                period = null;
            }
        }
        if (period == null) {
            final IPersistentPeriod persistentPeriod = persistentSupport.getIPersistentPeriod();
            period = (IPeriod) persistentPeriod.readByCalendarAndNextPeriod(examDate, examDate, null);
            if (period == null) {
                period = DomainFactory.makePeriod(examDate.getTime(), examDate.getTime());
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
