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
 * Created on Dec 9, 2005
 */
package org.fenixedu.academic.service.services.bolonhaManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.List;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLoad;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.bolonhaManager.CourseLoad;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditCompetenceCourseLoad {

    @Atomic
    public static void run(String competenceCourseID, RegimeType regimeType, Integer numberOfPeriods, List<CourseLoad> courseLoads)
            throws FenixServiceException {
        check(RolePredicates.BOLONHA_MANAGER_PREDICATE);
        final CompetenceCourse competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }
        competenceCourse.setRegime(regimeType);
        final AcademicPeriod academicPeriod = AcademicPeriod.SEMESTER;
        for (final CourseLoad courseLoad : courseLoads) {
            if (courseLoad.getAction().equals("create") && competenceCourse.getCompetenceCourseLoads().size() < numberOfPeriods) {
                competenceCourse.addCompetenceCourseLoad(courseLoad.getTheoreticalHours(), courseLoad.getProblemsHours(),
                        courseLoad.getLaboratorialHours(), courseLoad.getSeminaryHours(), courseLoad.getFieldWorkHours(),
                        courseLoad.getTrainingPeriodHours(), courseLoad.getTutorialOrientationHours(),
                        courseLoad.getAutonomousWorkHours(), courseLoad.getEctsCredits(), courseLoad.getOrder(), academicPeriod);
            } else {
                final CompetenceCourseLoad competenceCourseLoad = FenixFramework.getDomainObject(courseLoad.getIdentification());

                if (competenceCourseLoad != null && courseLoad.getAction().equals("edit")) {
                    competenceCourseLoad.edit(courseLoad.getTheoreticalHours(), courseLoad.getProblemsHours(),
                            courseLoad.getLaboratorialHours(), courseLoad.getSeminaryHours(), courseLoad.getFieldWorkHours(),
                            courseLoad.getTrainingPeriodHours(), courseLoad.getTutorialOrientationHours(),
                            courseLoad.getAutonomousWorkHours(), courseLoad.getEctsCredits(),
                            Integer.valueOf(courseLoad.getOrder()), academicPeriod);

                } else if (competenceCourseLoad != null && courseLoad.getAction().equals("delete")) {
                    competenceCourseLoad.delete();
                }
            }
        }
    }
}