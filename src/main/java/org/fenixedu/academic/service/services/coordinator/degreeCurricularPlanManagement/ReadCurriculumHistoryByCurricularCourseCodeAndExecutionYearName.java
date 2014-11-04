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
package org.fenixedu.academic.service.services.coordinator.degreeCurricularPlanManagement;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseScope;
import org.fenixedu.academic.domain.Curriculum;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.dto.InfoCurricularCourseScope;
import org.fenixedu.academic.dto.InfoCurriculum;
import org.fenixedu.academic.dto.InfoCurriculumWithInfoCurricularCourse;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério 17/Nov/2003
 */
public class ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName {

    @Atomic
    public static InfoCurriculum run(Integer executionDegreeCode, String curricularCourseCode, String stringExecutionYear)
            throws FenixServiceException {
        check(RolePredicates.COORDINATOR_PREDICATE);
        InfoCurriculum infoCurriculum = null;

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }
        if (stringExecutionYear == null || stringExecutionYear.length() == 0) {
            throw new FenixServiceException("nullExecutionYearName");
        }
        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("noCurricularCourse");
        }

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(stringExecutionYear);
        if (executionYear == null) {
            throw new NonExistingServiceException("noExecutionYear");
        }

        Curriculum curriculumExecutionYear = curricularCourse.findLatestCurriculumModifiedBefore(executionYear.getEndDate());
        if (curriculumExecutionYear != null) {
            List allCurricularCourseScopes = new ArrayList();
            List allExecutionCourses = new ArrayList();
            Collection executionPeriods = executionYear.getExecutionPeriodsSet();
            Iterator iterExecutionPeriods = executionPeriods.iterator();
            while (iterExecutionPeriods.hasNext()) {
                ExecutionSemester executionSemester = (ExecutionSemester) iterExecutionPeriods.next();
                Set<CurricularCourseScope> curricularCourseScopes =
                        curricularCourse.findCurricularCourseScopesIntersectingPeriod(executionSemester.getBeginDate(),
                                executionSemester.getEndDate());
                if (curricularCourseScopes != null) {
                    List disjunctionCurricularCourseScopes =
                            (List) CollectionUtils.disjunction(allCurricularCourseScopes, curricularCourseScopes);
                    List intersectionCurricularCourseScopes =
                            (List) CollectionUtils.intersection(allCurricularCourseScopes, curricularCourseScopes);

                    allCurricularCourseScopes =
                            (List) CollectionUtils.union(disjunctionCurricularCourseScopes, intersectionCurricularCourseScopes);
                }
                List associatedExecutionCourses = new ArrayList();
                Collection<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCoursesSet();
                for (ExecutionCourse executionCourse : executionCourses) {
                    if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                        associatedExecutionCourses.add(executionCourse);
                    }
                }

                if (associatedExecutionCourses != null) {
                    allExecutionCourses.addAll(associatedExecutionCourses);
                }

            }

            infoCurriculum = createInfoCurriculum(curriculumExecutionYear, allCurricularCourseScopes, allExecutionCourses);
        }
        return infoCurriculum;
    }

    private static InfoCurriculum createInfoCurriculum(Curriculum curriculum, List allCurricularCourseScopes,
            List allExecutionCourses) {

        InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum);

        List scopes = new ArrayList();
        CollectionUtils.collect(allCurricularCourseScopes, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;

                return InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
            }
        }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        Iterator iterExecutionCourses = allExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();
            infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}