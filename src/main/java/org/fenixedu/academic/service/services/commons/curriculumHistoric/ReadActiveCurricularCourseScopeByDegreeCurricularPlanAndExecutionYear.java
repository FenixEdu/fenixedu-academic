/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Oct 7, 2004
 */
package org.fenixedu.academic.service.services.commons.curriculumHistoric;

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear {

    @Atomic
    public static SortedSet<DegreeModuleScope> run(String degreeCurricularPlanID, AcademicInterval academicInterval)
            throws FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        final ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(new BeanComparator("curricularYear"));
        comparator.addComparator(new BeanComparator("curricularSemester"));
        comparator.addComparator(new BeanComparator("curricularCourse.externalId"));
        comparator.addComparator(new BeanComparator("branch"));

        final SortedSet<DegreeModuleScope> scopes = new TreeSet<DegreeModuleScope>(comparator);

        for (DegreeModuleScope degreeModuleScope : degreeCurricularPlan.getDegreeModuleScopes()) {
            if (degreeModuleScope.isActiveForAcademicInterval(academicInterval)) {
                scopes.add(degreeModuleScope);
            }
        }

        return scopes;
    }

    @Atomic
    @Deprecated
    public static SortedSet<DegreeModuleScope> run(String degreeCurricularPlanID, String executioYearID)
            throws FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executioYearID);

        final ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(new BeanComparator("curricularYear"));
        comparator.addComparator(new BeanComparator("curricularSemester"));
        comparator.addComparator(new BeanComparator("curricularCourse.externalId"));
        comparator.addComparator(new BeanComparator("branch"));

        final SortedSet<DegreeModuleScope> scopes = new TreeSet<DegreeModuleScope>(comparator);

        for (DegreeModuleScope degreeModuleScope : degreeCurricularPlan.getDegreeModuleScopes()) {
            if (degreeModuleScope.isActiveForExecutionYear(executionYear)) {
                scopes.add(degreeModuleScope);
            }
        }

        return scopes;
    }
}