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
 * Created on Oct 20, 2005
 *  by jdnf
 */

//TODO: DELETE
@Deprecated
public class ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear {

//    public List<ExecutionCourse> run(String degreeCurricularPlanID, String executionPeriodID, String curricularYearID)
//            throws FenixServiceException {
//
//        final ExecutionInterval executionSemester = FenixFramework.getDomainObject(executionPeriodID);
//        if (executionSemester == null) {
//            throw new FenixServiceException("error.no.executionPeriod");
//        }
//
//        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
//        if (degreeCurricularPlan == null) {
//            throw new FenixServiceException("error.coordinator.noDegreeCurricularPlan");
//        }
//
//        CurricularYear curricularYear = null;
//        if (curricularYearID != null) {
//            curricularYear = FenixFramework.getDomainObject(curricularYearID);
//            if (curricularYear == null) {
//                throw new FenixServiceException("error.no.curYear");
//            }
//        }
//
//        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
//        for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
//            if (belongToDegreeCurricularPlanAndCurricularYear(executionCourse, degreeCurricularPlan, curricularYear)) {
//                result.add(executionCourse);
//            }
//        }
//        return result;
//    }
//
//    private boolean belongToDegreeCurricularPlanAndCurricularYear(final ExecutionCourse executionCourse,
//            final DegreeCurricularPlan degreeCurricularPlan, final CurricularYear curricularYear) {
//
//        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
//            if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear, degreeCurricularPlan,
//                    executionCourse.getExecutionInterval())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Service Invokers migrated from Berserk
//
//    private static final ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear serviceInstance =
//            new ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear();
//
//    @Atomic
//    public static List<ExecutionCourse> runReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear(
//            String degreeCurricularPlanID, String executionPeriodID, String curricularYearID)
//            throws FenixServiceException, NotAuthorizedException {
//        try {
//            DegreeCurricularPlanAuthorizationFilter.instance.execute(degreeCurricularPlanID);
//            return serviceInstance.run(degreeCurricularPlanID, executionPeriodID, curricularYearID);
//        } catch (NotAuthorizedException ex1) {
//            try {
//                ResourceAllocationManagerAuthorizationFilter.instance.execute();
//                return serviceInstance.run(degreeCurricularPlanID, executionPeriodID, curricularYearID);
//            } catch (NotAuthorizedException ex2) {
//                throw ex2;
//            }
//        }
//    }

}