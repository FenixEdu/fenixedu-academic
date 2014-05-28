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
 *
 * Created on 2003/08/21
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorExecutionDegreeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.NumberUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SearchExecutionCourses {

    public List<InfoExecutionCourse> run(AcademicInterval academicInterval, ExecutionDegree executionDegree, String courseName) {
        List<ExecutionCourse> executionCourses =
                ExecutionCourse.searchByAcademicIntervalAndExecutionDegreeYearAndName(academicInterval, executionDegree, null,
                        courseName);
        return fillInfoExecutionCourses(academicInterval, executionCourses);
    }

    public List<InfoExecutionCourse> run(AcademicInterval academicInterval, ExecutionDegree executionDegree,
            CurricularYear curricularYear, String courseName) {
        List<ExecutionCourse> executionCourses =
                ExecutionCourse.searchByAcademicIntervalAndExecutionDegreeYearAndName(academicInterval, executionDegree,
                        curricularYear, courseName);
        return fillInfoExecutionCourses(academicInterval, executionCourses);
    }

    public List<InfoExecutionCourse> run(InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear, String executionCourseName) {

        List<InfoExecutionCourse> result = null;

        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());

        ExecutionDegree executionDegree = null;
        if (infoExecutionDegree != null) {
            executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        }

        CurricularYear curricularYear = null;
        if (infoCurricularYear != null) {
            curricularYear = FenixFramework.getDomainObject(infoCurricularYear.getExternalId());
        }

        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        if (executionSemester != null) {
            executionCourses =
                    executionSemester.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
                            executionDegree.getDegreeCurricularPlan(), curricularYear, executionCourseName);
        }

        return fillInfoExecutionCourses(executionSemester.getAcademicInterval(), executionCourses);
    }

    private List<InfoExecutionCourse> fillInfoExecutionCourses(final AcademicInterval academicInterval,
            List<ExecutionCourse> executionCourses) {
        List<InfoExecutionCourse> result;
        result = (List<InfoExecutionCourse>) CollectionUtils.collect(executionCourses, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                InfoExecutionCourse infoExecutionCourse = null;
                infoExecutionCourse = getOccupancyLevels(arg0);
                getTeacherReportInformation(infoExecutionCourse, arg0);
                return infoExecutionCourse;
            }

            private void getTeacherReportInformation(InfoExecutionCourse infoExecutionCourse, Object arg0) {

                ExecutionCourse executionCourse = (ExecutionCourse) arg0;

                if (executionCourse.getAssociatedCurricularCourses() != null) {

                    InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
                    int enrolledInCurricularCourse = 0;
                    int evaluated = 0;
                    int approved = 0;
                    Iterator<CurricularCourse> iter = executionCourse.getAssociatedCurricularCourses().iterator();

                    while (iter.hasNext()) {
                        CurricularCourse curricularCourse = iter.next();

                        final List<Enrolment> enroled = curricularCourse.getEnrolmentsByAcademicInterval(academicInterval);
                        enrolledInCurricularCourse += enroled.size();
                        evaluated = Enrolment.countEvaluated(enroled);
                        approved = Enrolment.countApproved(enroled);
                    }
                    infoSiteEvaluationStatistics.setEnrolled(Integer.valueOf(enrolledInCurricularCourse));
                    infoSiteEvaluationStatistics.setEvaluated(Integer.valueOf(evaluated));
                    infoSiteEvaluationStatistics.setApproved(Integer.valueOf(approved));

                    infoExecutionCourse.setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);
                }
            }

            private InfoExecutionCourse getOccupancyLevels(Object arg0) {

                InfoExecutionCourse infoExecutionCourse;
                ExecutionCourse executionCourse = (ExecutionCourse) arg0;

                Integer theoreticalCapacity = Integer.valueOf(0);
                Integer theoPraticalCapacity = Integer.valueOf(0);
                Integer praticalCapacity = Integer.valueOf(0);
                Integer labCapacity = Integer.valueOf(0);
                Integer doubtsCapacity = Integer.valueOf(0);
                Integer reserveCapacity = Integer.valueOf(0);

                Integer semCapacity = Integer.valueOf(0);
                Integer probCapacity = Integer.valueOf(0);
                Integer fieldCapacity = Integer.valueOf(0);
                Integer trainCapacity = Integer.valueOf(0);
                Integer tutCapacity = Integer.valueOf(0);

                Set<Shift> shifts = executionCourse.getAssociatedShifts();
                Iterator<Shift> iterator = shifts.iterator();

                while (iterator.hasNext()) {

                    Shift shift = iterator.next();

                    if (shift.containsType(ShiftType.TEORICA)) {
                        theoreticalCapacity = Integer.valueOf(theoreticalCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.TEORICO_PRATICA)) {
                        theoPraticalCapacity = Integer.valueOf(theoPraticalCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.DUVIDAS)) {
                        doubtsCapacity = Integer.valueOf(doubtsCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.LABORATORIAL)) {
                        labCapacity = Integer.valueOf(labCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.PRATICA)) {
                        praticalCapacity = Integer.valueOf(praticalCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.RESERVA)) {
                        reserveCapacity = Integer.valueOf(reserveCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.SEMINARY)) {
                        semCapacity = Integer.valueOf(semCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.PROBLEMS)) {
                        probCapacity = Integer.valueOf(probCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.FIELD_WORK)) {
                        fieldCapacity = Integer.valueOf(fieldCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.TRAINING_PERIOD)) {
                        trainCapacity = Integer.valueOf(trainCapacity.intValue() + shift.getLotacao().intValue());

                    } else if (shift.containsType(ShiftType.TUTORIAL_ORIENTATION)) {
                        tutCapacity = Integer.valueOf(tutCapacity.intValue() + shift.getLotacao().intValue());
                    }
                }

                infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                List<Integer> capacities = new ArrayList<Integer>();

                if (theoreticalCapacity.intValue() != 0) {
                    capacities.add(theoreticalCapacity);
                }
                if (theoPraticalCapacity.intValue() != 0) {
                    capacities.add(theoPraticalCapacity);
                }
                if (doubtsCapacity.intValue() != 0) {
                    capacities.add(doubtsCapacity);
                }
                if (labCapacity.intValue() != 0) {
                    capacities.add(labCapacity);
                }
                if (praticalCapacity.intValue() != 0) {
                    capacities.add(praticalCapacity);
                }
                if (reserveCapacity.intValue() != 0) {
                    capacities.add(reserveCapacity);
                }

                if (semCapacity.intValue() != 0) {
                    capacities.add(semCapacity);
                }
                if (probCapacity.intValue() != 0) {
                    capacities.add(probCapacity);
                }
                if (fieldCapacity.intValue() != 0) {
                    capacities.add(fieldCapacity);
                }
                if (trainCapacity.intValue() != 0) {
                    capacities.add(trainCapacity);
                }
                if (tutCapacity.intValue() != 0) {
                    capacities.add(tutCapacity);
                }

                int total = 0;

                if (!capacities.isEmpty()) {
                    total = (Collections.min(capacities)).intValue();
                }

                if (total == 0) {
                    infoExecutionCourse.setOccupancy(Double.valueOf(-1));
                } else {
                    infoExecutionCourse.setOccupancy(NumberUtils.formatNumber(
                            Double.valueOf((Double.valueOf(executionCourse.getAttends().size()).floatValue() * 100 / total)), 1));
                }
                return infoExecutionCourse;
            }
        });

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final SearchExecutionCourses serviceInstance = new SearchExecutionCourses();

    @Atomic
    public static List<InfoExecutionCourse> runSearchExecutionCourses(AcademicInterval academicInterval,
            ExecutionDegree executionDegree, String courseName) throws NotAuthorizedException {
        CoordinatorExecutionDegreeAuthorizationFilter.instance.execute(executionDegree.getExternalId());
        return serviceInstance.run(academicInterval, executionDegree, courseName);
    }

    @Atomic
    public static List<InfoExecutionCourse> runSearchExecutionCourses(InfoExecutionPeriod infoExecutionPeriod,
            InfoExecutionDegree infoExecutionDegree, InfoCurricularYear infoCurricularYear, String executionCourseName)
            throws NotAuthorizedException {
        CoordinatorExecutionDegreeAuthorizationFilter.instance.execute(infoExecutionDegree.getExternalId());
        return serviceInstance.run(infoExecutionPeriod, infoExecutionDegree, infoCurricularYear, executionCourseName);
    }

    @Atomic
    public static List<InfoExecutionCourse> runSearchExecutionCourses(AcademicInterval academicInterval,
            ExecutionDegree executionDegree, CurricularYear curricularYear, String courseName) throws NotAuthorizedException {
        CoordinatorExecutionDegreeAuthorizationFilter.instance.execute(executionDegree.getExternalId());
        return serviceInstance.run(academicInterval, executionDegree, curricularYear, courseName);
    }
}