package org.fenixedu.academic.domain.degreeStructure;

import java.math.BigDecimal;

import org.fenixedu.bennu.core.domain.Bennu;

public class CourseLoadDuration extends CourseLoadDuration_Base {

    protected CourseLoadDuration() {
        super();
        setRoot(Bennu.getInstance());
    }

    public static CourseLoadDuration create(final CompetenceCourseInformation courseInformation,
            final CourseLoadType courseLoadType, final BigDecimal hours) {

        if (courseInformation.findLoadDurationByType(courseLoadType).isPresent()) {
            throw new IllegalStateException("CourseLoadDuration already exists for course information and load type");
        }

        final CourseLoadDuration result = new CourseLoadDuration();
        result.setCompetenceCourseInformation(courseInformation);
        result.setCourseLoadType(courseLoadType);
        result.setHours(hours);

        return result;
    }

    @Override
    public void setHours(BigDecimal hours) {
        if (hours != null && hours.signum() < 0) {
            throw new IllegalArgumentException("CourseLoadDuration hours cannot be negative");
        }

        super.setHours(hours);
    }

    public void delete() {
        checkIfCanBeDeleted();

        setRoot(null);
        setCompetenceCourseInformation(null);
        setCourseLoadType(null);
        super.deleteDomainObject();
    }

    private void checkIfCanBeDeleted() {
        boolean shiftExistsForThisDurationLoadType = getCompetenceCourseInformation().getExecutionIntervalsRange().stream()
                .flatMap(ei -> ei.getAssociatedExecutionCoursesSet().stream()).flatMap(ec -> ec.getAssociatedShifts().stream())
                .anyMatch(s -> s.getCourseLoadType() == getCourseLoadType());

        if (shiftExistsForThisDurationLoadType) {
            throw new IllegalStateException("Shift exists for this duration load type");
        }
    }
}
