package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;

import org.joda.time.DateTime;

public class FenixCourseGroup {

    public static class GroupedCourse {

        String name;
        String degrees;
        String id;

        public GroupedCourse(final ExecutionCourse executionCourse) {
            this.name = executionCourse.getName();
            this.id = executionCourse.getExternalId();
            this.degrees = executionCourse.getDegreePresentationString();
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

        public String getDegrees() {
            return degrees;
        }

        public void setDegrees(String degrees) {
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

}
