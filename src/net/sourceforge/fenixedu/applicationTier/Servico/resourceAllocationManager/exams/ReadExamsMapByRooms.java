/*
 * ReadExamsMapByRoom.java
 * 
 * Created on 2004/02/19
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

/**
 * @author Ana e Ricardo
 * 
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExamsMapByRooms extends Service {

    public List<InfoRoomExamsMap> run(InfoExecutionPeriod infoExecutionPeriod, List<InfoRoom> infoRooms) throws Exception {
	final List<InfoRoomExamsMap> infoRoomExamMapList = new ArrayList<InfoRoomExamsMap>();

	final InfoPeriod period = calculateExamsSeason(infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod
		.getSemester().intValue());

	final Calendar startSeason1 = period.getStartDate();
	final Calendar endSeason2 = period.getEndDate();

	if (startSeason1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
	    // The calendar must start at a monday
	    int shiftDays = Calendar.MONDAY - startSeason1.get(Calendar.DAY_OF_WEEK);
	    startSeason1.add(Calendar.DATE, shiftDays);
	}

	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(infoExecutionPeriod
		.getIdInternal());

	for (final InfoRoom infoRoom : infoRooms) {
	    final InfoRoomExamsMap infoRoomExamsMap = new InfoRoomExamsMap();

	    infoRoomExamsMap.setInfoRoom(infoRoom);
	    infoRoomExamsMap.setStartSeason1(startSeason1);
	    infoRoomExamsMap.setEndSeason1(null);
	    infoRoomExamsMap.setStartSeason2(null);
	    infoRoomExamsMap.setEndSeason2(endSeason2);

	    // final List<Exam> exams =
	    // Exam.getAllByRoomAndExecutionPeriod(infoRoom.getNome(),
	    // infoExecutionPeriod.getName(),
	    // infoExecutionPeriod.getInfoExecutionYear().getYear());
	    // infoRoomExamsMap.setExams(getInfoExams(exams,
	    // infoExecutionPeriod));

	    infoRoomExamsMap.setExams(getInfoExams(infoRoom, executionSemester));

	    infoRoomExamMapList.add(infoRoomExamsMap);
	}
	return infoRoomExamMapList;
    }

    private List<InfoExam> getInfoExams(final InfoRoom infoRoom, final ExecutionSemester executionSemester) {
	final List<InfoExam> result = new ArrayList<InfoExam>();
	final AllocatableSpace oldRoom = (AllocatableSpace) rootDomainObject.readResourceByOID(infoRoom.getIdInternal());
	for (final ResourceAllocation roomOccupation : oldRoom.getResourceAllocations()) {
	    if (roomOccupation.isWrittenEvaluationSpaceOccupation()) {
		List<WrittenEvaluation> writtenEvaluations = ((WrittenEvaluationSpaceOccupation) roomOccupation)
			.getWrittenEvaluations();
		for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {
		    if (writtenEvaluation instanceof Exam) {
			final Exam exam = (Exam) writtenEvaluation;
			final Set<ExecutionCourse> executionCourses = exam.getAssociatedExecutionCoursesSet();
			final ExecutionCourse executionCourse = executionCourses.isEmpty() ? null : executionCourses.iterator()
				.next();
			if (executionCourse != null && executionSemester == executionCourse.getExecutionPeriod()) {
			    InfoExam infoExam = InfoExam.newInfoFromDomain(exam);
			    infoExam.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(exam
				    .getAssociatedExecutionCourses().get(0)));
			    result.add(infoExam);
			}
		    }
		}
	    }
	}
	return result;
    }

    // private List<InfoExam> getInfoExams(List<Exam> exams) {
    // final List<InfoExam> result = new ArrayList<InfoExam>(exams.size());
    // for (final Exam exam : exams) {
    // InfoExam infoExam = InfoExam.newInfoFromDomain(exam);
    // // Use one execution course
    // infoExam.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(exam
    // .getAssociatedExecutionCourses().get(0)));
    // result.add(infoExam);
    // }
    // return result;
    // }
    //
    private InfoPeriod calculateExamsSeason(final String year, final int semester) {

	ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);
	final List<ExecutionDegree> executionDegreesList = executionYear.getExecutionDegrees();

	Calendar startSeason1 = null, endSeason2 = null;
	Calendar startExams, endExams;

	for (final ExecutionDegree executionDegree : executionDegreesList) {
	    if (semester == 1) {
		startExams = executionDegree.getPeriodExamsFirstSemester().getStartDate();
		endExams = executionDegree.getPeriodExamsFirstSemester().getEndDateOfComposite();
	    } else {
		startExams = executionDegree.getPeriodExamsSecondSemester().getStartDate();
		endExams = executionDegree.getPeriodExamsSecondSemester().getEndDateOfComposite();
	    }
	    if (startSeason1 == null || startExams.before(startSeason1)) {
		startSeason1 = startExams;
	    }
	    if (endSeason2 == null || endExams.after(endSeason2)) {
		endSeason2 = endExams;
	    }
	}
	return new InfoPeriod(startSeason1, endSeason2);
    }
}