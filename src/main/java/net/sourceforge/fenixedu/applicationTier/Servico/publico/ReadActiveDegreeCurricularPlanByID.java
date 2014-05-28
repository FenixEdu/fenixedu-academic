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
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadDegreeCurricularPlanBaseService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério 5/Nov/2003
 * 
 */
public class ReadActiveDegreeCurricularPlanByID extends ReadDegreeCurricularPlanBaseService {

    protected List run(String degreeCurricularPlanId, String executionPeriodId, Locale locale, String arg)
            throws FenixServiceException {
        if (degreeCurricularPlanId == null) {
            throw new FenixServiceException("null degreeCurricularPlanId");
        }
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);

        if (executionPeriodId == null) {
            return groupScopesByCurricularYearAndCurricularCourseAndBranch(
                    super.readActiveCurricularCourseScopes(degreeCurricularPlanId), locale);
        }

        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);
        if (executionSemester == null || executionSemester.getExecutionYear() == null) {
            throw new FenixServiceException("null executionPeriod");
        }

        return groupScopesByCurricularYearAndCurricularCourseAndBranch(
                super.readActiveCurricularCourseScopesInExecutionYear(degreeCurricularPlan, executionSemester.getExecutionYear()),
                locale);
    }

    protected List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod, Integer curricularYear,
            Locale locale) throws FenixServiceException {
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("null degreeCurricularPlan");
        }

        if (infoExecutionPeriod == null) {
            return groupScopesByCurricularYearAndCurricularCourseAndBranch(
                    super.readActiveCurricularCourseScopes(degreeCurricularPlan.getExternalId()), locale);
        }
        ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
        if (executionSemester == null || executionSemester.getExecutionYear() == null) {
            throw new FenixServiceException("nullDegree");
        }

        return groupScopesByCurricularYearAndCurricularCourseAndBranch(
                super.readActiveCurricularCourseScopesInCurricularYearAndExecutionPeriodAndExecutionDegree(executionSemester,
                        executionDegree, curricularYear), locale);
    }

    private List groupScopesByCurricularYearAndCurricularCourseAndBranch(List<InfoCurricularCourseScope> scopes, Locale locale) {
        List result = new ArrayList();
        List<InfoCurricularCourseScope> temp = new ArrayList<InfoCurricularCourseScope>();

        List<InfoCurricularCourseScope> scopesAux = new ArrayList<InfoCurricularCourseScope>(scopes);
        Collections.sort(scopesAux, InfoCurricularCourseScope.COMPARATOR_BY_YEAR_SEMESTER_BRANCH_AND_NAME);

        if (scopesAux != null && scopesAux.size() > 0) {
            ListIterator iter = scopesAux.listIterator();
            InfoCurricularYear year = null;
            InfoCurricularCourse curricularCourse = null;

            while (iter.hasNext()) {
                InfoCurricularCourseScope scope = (InfoCurricularCourseScope) iter.next();
                InfoCurricularYear scopeYear = scope.getInfoCurricularSemester().getInfoCurricularYear();
                InfoCurricularCourse scopeCurricularCourse = scope.getInfoCurricularCourse();
                InfoBranch infoBranch = scope.getInfoBranch();
                if (year == null) {
                    year = scopeYear;
                }
                if (curricularCourse == null) {
                    curricularCourse = scopeCurricularCourse;
                }

                if (scopeYear.equals(year) && scopeCurricularCourse.equals(curricularCourse)) {
                    temp.add(scope);
                } else {
                    result.add(temp);
                    temp = new ArrayList<InfoCurricularCourseScope>();
                    year = scopeYear;
                    curricularCourse = scopeCurricularCourse;
                    temp.add(scope);
                }

                if (!iter.hasNext()) {
                    result.add(temp);
                }
            }
        }

        return result;
    }

    protected InfoDegreeCurricularPlan run(String degreeCurricularPlanId, Integer executionYear, String arg)
            throws FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("nullDegree");
        } else {
            return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
        }
    }

    // Service Invokers migrated from Berserk

    private static final ReadActiveDegreeCurricularPlanByID serviceInstance = new ReadActiveDegreeCurricularPlanByID();

    @Atomic
    public static List runReadActiveDegreeCurricularPlanByID(String degreeCurricularPlanId, String executionPeriodId,
            Locale locale, String arg) throws FenixServiceException {
        return serviceInstance.run(degreeCurricularPlanId, executionPeriodId, locale, arg);
    }

    @Atomic
    public static List runReadActiveDegreeCurricularPlanByID(InfoExecutionDegree infoExecutionDegree,
            InfoExecutionPeriod infoExecutionPeriod, Integer curricularYear, Locale locale) throws FenixServiceException {
        return serviceInstance.run(infoExecutionDegree, infoExecutionPeriod, curricularYear, locale);
    }

}