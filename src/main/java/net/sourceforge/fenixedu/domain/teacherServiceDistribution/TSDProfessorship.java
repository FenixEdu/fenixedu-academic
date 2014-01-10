package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.fenixedu.bennu.core.domain.Bennu;

public class TSDProfessorship extends TSDProfessorship_Base {

    private TSDProfessorship() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public TSDProfessorship(TSDCourse tsdCourse, TSDTeacher tsdTeacher, ShiftType type) {
        this();

        this.setTSDCourse(tsdCourse);
        this.setTSDTeacher(tsdTeacher);
        this.setType(type);
    }

    public Double getHours() {
        if (getHoursType() == TSDValueType.MANUAL_VALUE) {
            return super.getHoursManual();
        } else if (getHoursType() == TSDValueType.LAST_YEAR_REAL_VALUE) {
            return getTSDTeacher().getRealHoursByShiftTypeAndExecutionCourses(getType(),
                    getTSDCourse().getAssociatedExecutionCoursesLastYear());
        } else if (getHoursType() == TSDValueType.REAL_VALUE) {
            return getTSDTeacher().getRealHoursByShiftTypeAndExecutionCourses(getType(),
                    getTSDCourse().getAssociatedExecutionCourses());
        }

        return 0d;
    }

    public ExecutionSemester getExecutionPeriod() {
        return getTSDCourse().getExecutionPeriod();
    }

    public Boolean getIsActive() {
        return getTSDCourse().getIsActive();
    }

    public void delete() {
        setTSDCourse(null);
        setTSDTeacher(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasHoursManual() {
        return getHoursManual() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
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
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasTSDTeacher() {
        return getTSDTeacher() != null;
    }

}
