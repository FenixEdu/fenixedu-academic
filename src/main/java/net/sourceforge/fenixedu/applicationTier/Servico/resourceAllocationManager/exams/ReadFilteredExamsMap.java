/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.PublishedExamsMapAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadFilteredExamsMap {

    public class ExamsPeriodUndefined extends FenixServiceException {
        private static final long serialVersionUID = 1L;
    }

    protected InfoExamsMap run(InfoExecutionDegree infoExecutionDegree, List<Integer> curricularYears,
            InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {

        InfoExamsMap result = new InfoExamsMap();
        result.setInfoExecutionDegree(infoExecutionDegree);
        result.setInfoExecutionPeriod(infoExecutionPeriod);
        result.setCurricularYears(curricularYears);

        ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());

        obtainExamSeasonInfo(result, infoExecutionPeriod.getSemester(), executionDegree);

        // Obtain execution courses and associated information of the given
        // execution degree for each curricular year specified
        List<InfoExecutionCourse> infoExecutionCourses =
                obtainInfoExecutionCourses(curricularYears, infoExecutionPeriod, executionDegree);
        result.setExecutionCourses(infoExecutionCourses);

        User user = Authenticate.getUser();
        if (user == null || !user.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            PublishedExamsMapAuthorizationFilter.execute(result);
        }

        return result;
    }

    private void obtainExamSeasonInfo(InfoExamsMap result, Integer wantedSemester, ExecutionDegree executionDegree)
            throws ExamsPeriodUndefined {
        OccupationPeriod period = null;
        if (wantedSemester.equals(Integer.valueOf(1))) {
            period = executionDegree.getPeriodExamsFirstSemester();
        } else {
            period = executionDegree.getPeriodExamsSecondSemester();
        }

        if (period == null) {
            throw new ExamsPeriodUndefined();
        }

        Calendar startSeason1 = period.getStartDate();
        if (startSeason1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            // The calendar must start at a monday
            int shiftDays = Calendar.MONDAY - startSeason1.get(Calendar.DAY_OF_WEEK);
            startSeason1.add(Calendar.DATE, shiftDays);
        }
        result.setStartSeason1(startSeason1);
        result.setEndSeason1(null);
        result.setStartSeason2(null);

        Calendar endSeason2 = period.getEndDateOfComposite();
        result.setEndSeason2(endSeason2);
    }

    private List<InfoExecutionCourse> obtainInfoExecutionCourses(List<Integer> curricularYears,
            InfoExecutionPeriod infoExecutionPeriod, ExecutionDegree executionDegree) {
        List<InfoExecutionCourse> result = new ArrayList<InfoExecutionCourse>();
        ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
        for (Integer curricularYear : curricularYears) {
            // Obtain list of execution courses
            List<ExecutionCourse> executionCourses =
                    executionDegree.getDegreeCurricularPlan().getExecutionCoursesByExecutionPeriodAndSemesterAndYear(
                            executionSemester, curricularYear, infoExecutionPeriod.getSemester());

            // For each execution course obtain curricular courses and exams
            for (ExecutionCourse executionCourse : executionCourses) {
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                infoExecutionCourse.setCurricularYear(curricularYear);

                List<InfoExam> associatedInfoExams =
                        obtainInfoExams(executionDegree, infoExecutionPeriod.getExternalId(), curricularYear, executionCourse);
                infoExecutionCourse.setFilteredAssociatedInfoExams(associatedInfoExams);

                result.add(infoExecutionCourse);
            }
        }
        return result;
    }

    private List<InfoExam> obtainInfoExams(ExecutionDegree executionDegree, String executionPeriodId,
            Integer wantedCurricularYear, ExecutionCourse executionCourse) {
        List<InfoExam> result = new ArrayList<InfoExam>();
        for (Exam exam : executionCourse.getAssociatedExams()) {
            InfoExam infoExam =
                    InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear.newInfoFromDomain(exam);
            int numberOfStudentsForExam = 0;
            Set<CurricularCourse> checkedCurricularCourses = new HashSet<CurricularCourse>();
            for (DegreeModuleScope degreeModuleScope : exam.getDegreeModuleScopes()) {
                CurricularCourse curricularCourse = degreeModuleScope.getCurricularCourse();
                if (!checkedCurricularCourses.contains(curricularCourse)) {
                    checkedCurricularCourses.add(curricularCourse);
                    ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);
                    int numberEnroledStudentsInCurricularCourse =
                            curricularCourse.countEnrolmentsByExecutionPeriod(executionSemester);
                    numberOfStudentsForExam += numberEnroledStudentsInCurricularCourse;
                }

                boolean isCurricularYearEqual = degreeModuleScope.getCurricularYear().equals(wantedCurricularYear);
                DegreeCurricularPlan degreeCurricularPlanFromScope =
                        degreeModuleScope.getCurricularCourse().getDegreeCurricularPlan();
                DegreeCurricularPlan degreeCurricularPlanFromExecutionDegree = executionDegree.getDegreeCurricularPlan();
                boolean isCurricularPlanEqual = degreeCurricularPlanFromScope.equals(degreeCurricularPlanFromExecutionDegree);

                if (isCurricularYearEqual && isCurricularPlanEqual && !result.contains(infoExam)) {
                    result.add(infoExam);
                    break;
                }
            }
            infoExam.setEnrolledStudents(Integer.valueOf(numberOfStudentsForExam));
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadFilteredExamsMap serviceInstance = new ReadFilteredExamsMap();

    @Atomic
    public static InfoExamsMap runReadFilteredExamsMap(InfoExecutionDegree infoExecutionDegree, List<Integer> curricularYears,
            InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {
        return serviceInstance.run(infoExecutionDegree, curricularYears, infoExecutionPeriod);
    }

}