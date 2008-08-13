/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/04/04
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.Season;

public class ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod extends Service {

    public InfoViewExamByDayAndShift run(String executionCourseInitials, Season season, InfoExecutionPeriod infoExecutionPeriod)
	    {
	InfoViewExamByDayAndShift infoViewExamByDayAndShift = new InfoViewExamByDayAndShift();

	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(infoExecutionPeriod.getIdInternal());
	ExecutionCourse executionCourse = executionSemester.getExecutionCourseByInitials(executionCourseInitials);

	List<Exam> associatedExams = new ArrayList();
	List<Evaluation> associatedEvaluations = executionCourse.getAssociatedEvaluations();
	for (Evaluation evaluation : associatedEvaluations) {
	    if (evaluation instanceof Exam) {
		associatedExams.add((Exam) evaluation);
	    }
	}
	for (int i = 0; i < associatedExams.size(); i++) {
	    Exam exam = associatedExams.get(i);
	    if (exam.getSeason().equals(season)) {
		infoViewExamByDayAndShift.setInfoExam(InfoExam.newInfoFromDomain(exam));

		List infoExecutionCourses = new ArrayList();
		List infoDegrees = new ArrayList();
		for (int j = 0; j < exam.getAssociatedExecutionCourses().size(); j++) {
		    ExecutionCourse tempExecutionCourse = exam.getAssociatedExecutionCourses().get(j);
		    infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(tempExecutionCourse));

		    // prepare degrees associated with exam
		    List tempAssociatedCurricularCourses = executionCourse.getAssociatedCurricularCourses();
		    for (int k = 0; k < tempAssociatedCurricularCourses.size(); k++) {
			Degree tempDegree = ((CurricularCourse) tempAssociatedCurricularCourses.get(k)).getDegreeCurricularPlan()
				.getDegree();
			infoDegrees.add(InfoDegree.newInfoFromDomain(tempDegree));
		    }
		}
		infoViewExamByDayAndShift.setInfoExecutionCourses(infoExecutionCourses);
		infoViewExamByDayAndShift.setInfoDegrees(infoDegrees);
	    }
	}

	return infoViewExamByDayAndShift;
    }
}