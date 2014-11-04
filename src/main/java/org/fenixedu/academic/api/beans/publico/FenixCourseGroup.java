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
package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;

import org.joda.time.DateTime;

public class FenixCourseGroup {

    public static class GroupedCourse {

        String name;
        List<FenixDegree> degrees;
        String id;

        public GroupedCourse(final ExecutionCourse executionCourse) {
            setName(executionCourse.getName());
            setId(executionCourse.getExternalId());
            setDegrees(executionCourse);
        }

        private void setDegrees(ExecutionCourse executionCourse) {
            this.degrees = new ArrayList<>();
            for (Degree degree : executionCourse.getDegreesSortedByDegreeName()) {
                degrees.add(new FenixDegree(degree));
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<FenixDegree> getDegrees() {
            return degrees;
        }

        public void setDegrees(List<FenixDegree> degrees) {
            this.degrees = degrees;
        }

    }

    String name;
    String description;
    FenixInterval enrolmentPeriod;
    String enrolmentPolicy;
    Integer minimumCapacity;
    Integer maximumCapacity;
    Integer idealCapacity;
    List<GroupedCourse> associatedCourses = new ArrayList<>();

    public FenixCourseGroup(final Grouping grouping) {
        this.name = grouping.getName();
        this.description = grouping.getProjectDescription();

        final DateTime start = grouping.getEnrolmentBeginDayDateDateTime();
        final DateTime end = grouping.getEnrolmentEndDayDateDateTime();
        this.enrolmentPeriod = new FenixInterval(start, end);

        final EnrolmentGroupPolicyType enrolmentPolicy = grouping.getEnrolmentPolicy();
        this.enrolmentPolicy = enrolmentPolicy == null ? null : enrolmentPolicy.getTypeFullName();

        this.minimumCapacity = grouping.getMinimumCapacity();
        this.maximumCapacity = grouping.getMaximumCapacity();
        this.idealCapacity = grouping.getIdealCapacity();

        for (final ExportGrouping exportGrouping : grouping.getExportGroupingsSet()) {
            final ExecutionCourse executionCourse = exportGrouping.getExecutionCourse();
            associatedCourses.add(new GroupedCourse(executionCourse));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public FenixInterval getEnrolmentPeriod() {
        return enrolmentPeriod;
    }

    public void setEnrolmentPeriod(final FenixInterval enrolmentPeriod) {
        this.enrolmentPeriod = enrolmentPeriod;
    }

    public String getEnrolmentPolicy() {
        return enrolmentPolicy;
    }

    public void setEnrolmentPolicy(final String enrolmentPolicy) {
        this.enrolmentPolicy = enrolmentPolicy;
    }

    public Integer getMinimumCapacity() {
        return minimumCapacity;
    }

    public void setMinimumCapacity(final Integer minimumCapacity) {
        this.minimumCapacity = minimumCapacity;
    }

    public Integer getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(final Integer maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public Integer getIdealCapacity() {
        return idealCapacity;
    }

    public void setIdealCapacity(final Integer idealCapacity) {
        this.idealCapacity = idealCapacity;
    }

    public List<GroupedCourse> getAssociatedCourses() {
        return associatedCourses;
    }

    public void setAssociatedCourses(List<GroupedCourse> associatedCourses) {
        this.associatedCourses = associatedCourses;
    }

}
