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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilancyCourseGroupBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCoursesForUnit implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) source;

        Unit unit = bean.getSelectedUnit();
        Department department = bean.getSelectedDepartment();
        CompetenceCourseGroupUnit competenceCourseGroup = bean.getSelectedCompetenceCourseGroupUnit();

        Set<ExecutionCourse> courses = new HashSet<ExecutionCourse>();
        if (unit == null) {
            // Add pre-bolonha competenceCourses
            courses.addAll(getExecutionCoursesFromCompetenceCourses(department.getCompetenceCourses()));
            unit = department.getDepartmentUnit();
        }

        // Add bolonha competenceCourses
        courses.addAll((competenceCourseGroup == null) ? getBolonhaCourses(unit) : getBolonhaCoursesForGivenGroup(competenceCourseGroup));

        List<ExecutionCourse> coursesToProvide = new ArrayList<ExecutionCourse>(courses);
        Collections.sort(coursesToProvide, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        return coursesToProvide;
    }

    private List<ExecutionCourse> getBolonhaCoursesForGivenGroup(CompetenceCourseGroupUnit competenceCourseGroup) {
        return getExecutionCoursesFromCompetenceCourses(competenceCourseGroup.getCompetenceCourses());
    }

    private List<ExecutionCourse> getBolonhaCourses(Unit unit) {

        List<CompetenceCourseGroupUnit> courseGroups = new ArrayList<CompetenceCourseGroupUnit>();
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        if (unit.isDepartmentUnit()) {
            List<ScientificAreaUnit> scientificAreaUnits = ((DepartmentUnit) unit).getScientificAreaUnits();
            for (ScientificAreaUnit areaUnit : scientificAreaUnits) {
                courseGroups.addAll(areaUnit.getCompetenceCourseGroupUnits());
            }
        } else {
            courseGroups.addAll(((ScientificAreaUnit) unit).getCompetenceCourseGroupUnits());
        }

        for (CompetenceCourseGroupUnit courseGroup : courseGroups) {
            executionCourses.addAll(getBolonhaCoursesForGivenGroup(courseGroup));
        }

        return executionCourses;
    }

    private List<ExecutionCourse> getExecutionCoursesFromCompetenceCourses(Collection<CompetenceCourse> competenceCourses) {
        List<ExecutionCourse> courses = new ArrayList<ExecutionCourse>();
        ExecutionSemester period = ExecutionSemester.readActualExecutionSemester();
        for (CompetenceCourse course : competenceCourses) {
            courses.addAll(course.getExecutionCoursesByExecutionPeriod(period));
        }
        return courses;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
