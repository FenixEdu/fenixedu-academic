package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExamsByDate implements IService {

    public InfoViewExam run(Calendar examDay, Calendar examStartTime, Calendar examEndTime)
            throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final List<IExam> filteredExams = persistentSupport.getIPersistentExam().readBy(examDay,
                examStartTime, examEndTime);

        final InfoViewExam infoViewExam = new InfoViewExam();
        List<InfoViewExamByDayAndShift> infoViewExamsByDayAndShiftList = new ArrayList<InfoViewExamByDayAndShift>();
        infoViewExam.setInfoViewExamsByDayAndShift(infoViewExamsByDayAndShiftList);

        for (final IExam exam : filteredExams) {
            final InfoViewExamByDayAndShift viewExamByDayAndShift = new InfoViewExamByDayAndShift();

            final InfoExam infoExam = InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear
                    .newInfoFromDomain(exam);
            final List<InfoExecutionCourse> infoExecutionCourses = readInfoExecutionCourses(exam);
            final List<InfoDegree> infoDegrees = readInfoDegrees(exam, viewExamByDayAndShift);
            final Integer availableRoomOccupation = calculateAvailableRoomOccupation(exam,
                    viewExamByDayAndShift.getNumberStudentesAttendingCourse());

            viewExamByDayAndShift.setInfoExam(infoExam);
            viewExamByDayAndShift.setInfoExecutionCourses(infoExecutionCourses);
            viewExamByDayAndShift.setInfoDegrees(infoDegrees);
            viewExamByDayAndShift.setAvailableRoomOccupation(availableRoomOccupation);
            infoViewExamsByDayAndShiftList.add(viewExamByDayAndShift);
        }
        return infoViewExam;
    }

    private Integer calculateAvailableRoomOccupation(final IExam exam,
            final Integer numberStudentesAttendingCourse) {
        int totalExamCapacity = 0;
        for (final IRoomOccupation roomOccupation : exam.getAssociatedRoomOccupation()) {
            totalExamCapacity += roomOccupation.getRoom().getCapacidadeExame().intValue();
        }
        return Integer.valueOf(numberStudentesAttendingCourse.intValue() - totalExamCapacity);
    }

    private List<InfoDegree> readInfoDegrees(final IExam exam,
            InfoViewExamByDayAndShift viewExamByDayAndShift) {

        final List<InfoDegree> result = new ArrayList<InfoDegree>();
        final Set<Integer> curricularCourseIDs = new HashSet<Integer>();

        // Select an ExecutionPeriod from any ExecutionCourses
        final IExecutionPeriod executionPeriod = exam.getAssociatedExecutionCourses().get(0)
                .getExecutionPeriod();
        int numberStudentes = 0;

        for (final ICurricularCourseScope curricularCourseScope : exam
                .getAssociatedCurricularCourseScope()) {
            final ICurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();
            if (!curricularCourseIDs.contains(curricularCourse.getIdInternal())) {
                curricularCourseIDs.add(curricularCourse.getIdInternal());
                result.add(InfoDegree.newInfoFromDomain(curricularCourse.getDegreeCurricularPlan()
                        .getDegree()));
                numberStudentes += calculateNumberOfEnrolmentStudents(curricularCourse, executionPeriod);
            }
        }
        viewExamByDayAndShift.setNumberStudentesAttendingCourse(Integer.valueOf(numberStudentes));

        return result;
    }

    private Integer calculateNumberOfEnrolmentStudents(final ICurricularCourse curricularCourse,
            final IExecutionPeriod executionPeriod) {
        int numberOfStudents = 0;
        for (final IEnrolment enrolment : curricularCourse.getEnrolments()) {
            if (enrolment.getExecutionPeriod() == executionPeriod) {
                numberOfStudents++;
            }
        }
        return Integer.valueOf(numberOfStudents);
    }

    private List<InfoExecutionCourse> readInfoExecutionCourses(final IExam exam) {
        final List<InfoExecutionCourse> result = new ArrayList<InfoExecutionCourse>(exam
                .getAssociatedExecutionCoursesCount());
        for (final IExecutionCourse executionCourse : exam.getAssociatedExecutionCourses()) {
            result.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }
        return result;
    }
}