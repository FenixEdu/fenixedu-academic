package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class TSDCurricularLoad extends TSDCurricularLoad_Base {

    public enum SchoolClassCalculationMethod {
        SHIFT_BASED, SHIFT_AND_FREQUENCY_BASED;
    }

    private TSDCurricularLoad() {
        super();
    }

    protected TSDCurricularLoad(TSDCourse course, ShiftType type, Double hoursManual, TSDValueType hoursType,
            Integer studentsPerShiftManual, TSDValueType studentsPerShiftType,
            Double weightFirstTimeEnrolledStudentsPerShiftManual, TSDValueType weightFirstTimeEnrolledStudentsPerShiftType,
            Double weightSecondTimeEnrolledStudentsPerShiftManual, TSDValueType weightSecondTimeEnrolledStudentsPerShiftType) {

        this();

        if (course == null) {
            throw new DomainException("TSDCourse.required");
        }

        super.setTSDCourse(course);
        super.setType(type);
        super.setHoursManual(hoursManual);
        super.setHoursType(hoursType);
        super.setStudentsPerShiftManual(studentsPerShiftManual);
        super.setStudentsPerShiftType(studentsPerShiftType);
        super.setWeightFirstTimeEnrolledStudentsPerShiftManual(weightFirstTimeEnrolledStudentsPerShiftManual);
        super.setWeightFirstTimeEnrolledStudentsPerShiftType(weightFirstTimeEnrolledStudentsPerShiftType);
        super.setWeightSecondTimeEnrolledStudentsPerShiftManual(weightSecondTimeEnrolledStudentsPerShiftManual);
        super.setWeightSecondTimeEnrolledStudentsPerShiftType(weightSecondTimeEnrolledStudentsPerShiftType);
        super.setFrequency(1d);
        this.setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        setTSDCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Double getHoursPerShift() {
        Double hoursPerShift = super.getHoursPerShiftManual();

        if (hoursPerShift == null) {
            TSDCourse course = getTSDCourse();
            List<CurricularCourse> courseList = course == null ? null : course.getAssociatedCurricularCourses();

            BigDecimal shiftHours = null;

            if (courseList != null && !courseList.isEmpty()) {
                shiftHours = courseList.get(0).getTotalHoursByShiftType(getType(), course.getExecutionPeriod());
            }
            return shiftHours == null ? 0d : shiftHours.doubleValue() / CompetenceCourseLoad.NUMBER_OF_WEEKS;
        }

        return hoursPerShift;
    }

    public Integer getTimeTableSlotsNumber() {
        Integer slots = super.getTimeTableSlots();
        ShiftType type = getType();
        TSDCourse course = getTSDCourse();
        Double frequency = getFrequency();

        double numberOfShifts =
                StrictMath.ceil(((new Double(course.getTotalNumberOfStudents(type)) / course.getStudentsPerShift(type))));

        Double slotsCalculated = Math.ceil(new Double(frequency * numberOfShifts));

        if (slots == null || slots.equals(0) || slots > slotsCalculated) {
            return slotsCalculated.intValue();
        } else {
            return slots;
        }
    }

    public Integer getStudentsPerShift() {
        return getTSDCourse().getStudentsPerShift(getType());
    }

    public Double getHours() {
        return getTSDCourse().getHours(getType());
    }

    public Double getWeightFirstTimeEnrolledStudentsPerShift() {
        return getTSDCourse().getWeightFirstTimeEnrolledStudentsPerShift(getType());
    }

    public Double getWeightSecondTimeEnrolledStudentsPerShift() {
        return getTSDCourse().getWeightSecondTimeEnrolledStudentsPerShift(getType());
    }

    public Integer getNumberOfShifts() {
        return getTSDCourse().getNumberOfShifts(getType());
    }

    public double getNumberOfHoursForStudents() {
        return getTSDCourse().getNumberOfHoursForStudents(getType());
    }

    public double getNumberOfHoursForTeachers() {
        return getTSDCourse().getNumberOfHoursForTeachers(getType());
    }

    public Integer getNumberOfSchoolClasses() {
        return new Double(
                Math.ceil((SchoolClassCalculationMethod.SHIFT_BASED.equals(getSchoolClassCalculationMethod())) ? getNumberOfShifts() : getNumberOfShifts()
                        * getFrequency())).intValue();
    }
    @Deprecated
    public boolean hasHoursManual() {
        return getHoursManual() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasWeightFirstTimeEnrolledStudentsPerShiftManual() {
        return getWeightFirstTimeEnrolledStudentsPerShiftManual() != null;
    }

    @Deprecated
    public boolean hasHoursPerShiftManual() {
        return getHoursPerShiftManual() != null;
    }

    @Deprecated
    public boolean hasStudentsPerShiftManual() {
        return getStudentsPerShiftManual() != null;
    }

    @Deprecated
    public boolean hasWeightSecondTimeEnrolledStudentsPerShiftType() {
        return getWeightSecondTimeEnrolledStudentsPerShiftType() != null;
    }

    @Deprecated
    public boolean hasHoursType() {
        return getHoursType() != null;
    }

    @Deprecated
    public boolean hasTSDCourse() {
        return getTSDCourse() != null;
    }

    @Deprecated
    public boolean hasSchoolClassCalculationMethod() {
        return getSchoolClassCalculationMethod() != null;
    }

    @Deprecated
    public boolean hasTimeTableSlots() {
        return getTimeTableSlots() != null;
    }

    @Deprecated
    public boolean hasStudentsPerShiftType() {
        return getStudentsPerShiftType() != null;
    }

    @Deprecated
    public boolean hasFrequency() {
        return getFrequency() != null;
    }

    @Deprecated
    public boolean hasWeightSecondTimeEnrolledStudentsPerShiftManual() {
        return getWeightSecondTimeEnrolledStudentsPerShiftManual() != null;
    }

    @Deprecated
    public boolean hasWeightFirstTimeEnrolledStudentsPerShiftType() {
        return getWeightFirstTimeEnrolledStudentsPerShiftType() != null;
    }

}
