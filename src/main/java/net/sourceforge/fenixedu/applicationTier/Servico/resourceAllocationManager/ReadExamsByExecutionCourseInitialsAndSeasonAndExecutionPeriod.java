/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/04/04
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Season;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod {

    @Atomic
    public static InfoViewExamByDayAndShift run(String executionCourseInitials, Season season,
            InfoExecutionPeriod infoExecutionPeriod) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        InfoViewExamByDayAndShift infoViewExamByDayAndShift = new InfoViewExamByDayAndShift();

        ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
        ExecutionCourse executionCourse = executionSemester.getExecutionCourseByInitials(executionCourseInitials);

        List<Exam> associatedExams = new ArrayList();
        Collection<Evaluation> associatedEvaluations = executionCourse.getAssociatedEvaluationsSet();
        for (Evaluation evaluation : associatedEvaluations) {
            if (evaluation instanceof Exam) {
                associatedExams.add((Exam) evaluation);
            }
        }
        for (Exam exam : associatedExams) {
            if (exam.getSeason().equals(season)) {
                infoViewExamByDayAndShift.setInfoExam(InfoExam.newInfoFromDomain(exam));

                List infoExecutionCourses = new ArrayList();
                List infoDegrees = new ArrayList();
                for (ExecutionCourse tempExecutionCourse : exam.getAssociatedExecutionCoursesSet()) {
                    infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(tempExecutionCourse));

                    // prepare degrees associated with exam
                    Collection<CurricularCourse> tempAssociatedCurricularCourses =
                            executionCourse.getAssociatedCurricularCoursesSet();
                    for (CurricularCourse curricularCourse : tempAssociatedCurricularCourses) {
                        Degree tempDegree = curricularCourse.getDegreeCurricularPlan().getDegree();
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