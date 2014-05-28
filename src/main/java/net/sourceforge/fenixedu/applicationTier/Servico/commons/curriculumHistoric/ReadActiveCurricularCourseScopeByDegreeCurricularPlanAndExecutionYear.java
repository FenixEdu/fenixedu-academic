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
/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Filtro.AcademicCurriculumsViewAuthorization;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ResourceAllocationManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear {

    public SortedSet<DegreeModuleScope> run(String degreeCurricularPlanID, AcademicInterval academicInterval)
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

    @Deprecated
    public SortedSet<DegreeModuleScope> run(String degreeCurricularPlanID, String executioYearID) throws FenixServiceException {
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

    // Service Invokers migrated from Berserk

    private static final ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear serviceInstance =
            new ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear();

    @Atomic
    public static SortedSet<DegreeModuleScope> runReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear(
            String degreeCurricularPlanID, AcademicInterval academicInterval) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(degreeCurricularPlanID, academicInterval);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(degreeCurricularPlanID, academicInterval);
            } catch (NotAuthorizedException ex2) {
                try {
                    AcademicCurriculumsViewAuthorization.instance.execute();
                    return serviceInstance.run(degreeCurricularPlanID, academicInterval);
                } catch (NotAuthorizedException ex3) {
                    try {
                        ResourceAllocationManagerAuthorizationFilter.instance.execute();
                        return serviceInstance.run(degreeCurricularPlanID, academicInterval);
                    } catch (NotAuthorizedException ex4) {
                        try {
                            GEPAuthorizationFilter.instance.execute();
                            return serviceInstance.run(degreeCurricularPlanID, academicInterval);
                        } catch (NotAuthorizedException ex5) {
                            try {
                                DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                                EmployeeAuthorizationFilter.instance.execute();
                                return serviceInstance.run(degreeCurricularPlanID, academicInterval);
                            } catch (NotAuthorizedException ex6) {
                                try {
                                    OperatorAuthorizationFilter.instance.execute();
                                    return serviceInstance.run(degreeCurricularPlanID, academicInterval);
                                } catch (NotAuthorizedException ex7) {
                                    throw ex7;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Atomic
    public static SortedSet<DegreeModuleScope> runReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear(
            String degreeCurricularPlanID, String executioYearID) throws FenixServiceException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(degreeCurricularPlanID, executioYearID);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(degreeCurricularPlanID, executioYearID);
            } catch (NotAuthorizedException ex2) {
                try {
                    AcademicCurriculumsViewAuthorization.instance.execute();
                    return serviceInstance.run(degreeCurricularPlanID, executioYearID);
                } catch (NotAuthorizedException ex3) {
                    try {
                        ResourceAllocationManagerAuthorizationFilter.instance.execute();
                        return serviceInstance.run(degreeCurricularPlanID, executioYearID);
                    } catch (NotAuthorizedException ex4) {
                        try {
                            GEPAuthorizationFilter.instance.execute();
                            return serviceInstance.run(degreeCurricularPlanID, executioYearID);
                        } catch (NotAuthorizedException ex5) {
                            try {
                                DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                                EmployeeAuthorizationFilter.instance.execute();
                                return serviceInstance.run(degreeCurricularPlanID, executioYearID);
                            } catch (NotAuthorizedException ex6) {
                                try {
                                    OperatorAuthorizationFilter.instance.execute();
                                    return serviceInstance.run(degreeCurricularPlanID, executioYearID);
                                } catch (NotAuthorizedException ex7) {
                                    throw ex7;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}