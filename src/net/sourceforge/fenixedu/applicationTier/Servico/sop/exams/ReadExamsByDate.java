package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;

public class ReadExamsByDate extends Service {

    public InfoViewExam run(Calendar examDay, Calendar examStartTime, Calendar examEndTime) {
        
        final List<Exam> filteredExams = Exam.getAllByDate(examDay, examStartTime, examEndTime);

        final InfoViewExam infoViewExam = new InfoViewExam();
        List<InfoViewExamByDayAndShift> infoViewExamsByDayAndShiftList = new ArrayList<InfoViewExamByDayAndShift>();
        infoViewExam.setInfoViewExamsByDayAndShift(infoViewExamsByDayAndShiftList);

        for (final Exam exam : filteredExams) {
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

    private Integer calculateAvailableRoomOccupation(final Exam exam,
            final Integer numberStudentesAttendingCourse) {
        int totalExamCapacity = 0;
        for (final RoomOccupation roomOccupation : exam.getAssociatedRoomOccupation()) {
            totalExamCapacity += ((OldRoom)roomOccupation.getRoom()).getCapacidadeExame().intValue();
        }
        return Integer.valueOf(numberStudentesAttendingCourse.intValue() - totalExamCapacity);
    }

    private List<InfoDegree> readInfoDegrees(final Exam exam,
            InfoViewExamByDayAndShift viewExamByDayAndShift) {

        final List<InfoDegree> result = new ArrayList<InfoDegree>();
        final Set<Integer> curricularCourseIDs = new HashSet<Integer>();

        // Select an ExecutionPeriod from any ExecutionCourses
        final ExecutionPeriod executionPeriod = exam.getAssociatedExecutionCourses().get(0)
                .getExecutionPeriod();
        int numberStudentes = 0;

        for (final DegreeModuleScope degreeModuleScope : exam.getDegreeModuleScopes()) {
            final CurricularCourse curricularCourse = degreeModuleScope.getCurricularCourse();
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

    private Integer calculateNumberOfEnrolmentStudents(final CurricularCourse curricularCourse,
            final ExecutionPeriod executionPeriod) {
        int numberOfStudents = 0;
        for (final Enrolment enrolment : curricularCourse.getEnrolments()) {
            if (enrolment.getExecutionPeriod() == executionPeriod) {
                numberOfStudents++;
            }
        }
        return Integer.valueOf(numberOfStudents);
    }

    private List<InfoExecutionCourse> readInfoExecutionCourses(final Exam exam) {
        final List<InfoExecutionCourse> result = new ArrayList<InfoExecutionCourse>(exam
                .getAssociatedExecutionCoursesCount());
        for (final ExecutionCourse executionCourse : exam.getAssociatedExecutionCourses()) {
            result.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }
        return result;
    }
}