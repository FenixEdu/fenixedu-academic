package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.tools.enrollment.AreaType;

/**
 * @author David Santos on Jul 26, 2004
 */

public class OptionalCurricularCourseGroup extends OptionalCurricularCourseGroup_Base {

    public Integer getMinimumNumberOfOptionalCourses() {
        return super.getMinimumValue();
    }

    public Integer getMaximumNumberOfOptionalCourses() {
        return super.getMaximumValue();
    }

    public void setMaximumNumberOfOptionalCourses(Integer maximumNumberOfOptionalCourses) {
        super.setMaximumValue(maximumNumberOfOptionalCourses);
    }

    public void setMinimumNumberOfOptionalCourses(Integer minimumNumberOfOptionalCourses) {
        super.setMinimumValue(minimumNumberOfOptionalCourses);
    }

    public Integer getMaximumCredits() {
        return null;
    }

    public void setMaximumCredits(Integer maximumCredits) {
    }

    public Integer getMinimumCredits() {
        return null;
    }

    public void setMinimumCredits(Integer minimumCredits) {
    }

    public AreaType getAreaType() {
        return null;
    }

    public void setAreaType(AreaType areaType) {
    }

    public String toString() {
        return "minimumNumberOfOptionalCourses[" + super.getMinimumValue()
                + "] maximumNumberOfOptionalCourses[" + super.getMaximumValue() + "] branch["
                + getBranch().getName() + "]";
    }

}