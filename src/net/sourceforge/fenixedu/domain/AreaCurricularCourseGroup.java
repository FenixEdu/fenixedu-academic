package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author David Santos on Jul 26, 2004
 */

public class AreaCurricularCourseGroup extends AreaCurricularCourseGroup_Base {

    public Integer getMaximumCredits() {
        return super.getMaximumValue();
    }

    public void setMaximumCredits(Integer maximumCredits) {
        super.setMaximumValue(maximumCredits);
    }

    public Integer getMinimumCredits() {
        return super.getMinimumValue();
    }

    public void setMinimumCredits(Integer minimumCredits) {
        super.setMinimumValue(minimumCredits);
    }

    public Integer getMinimumNumberOfOptionalCourses() {
        return null;
    }

    public Integer getMaximumNumberOfOptionalCourses() {
        return null;
    }

    public void setMaximumNumberOfOptionalCourses(Integer maximumNumberOfOptionalCourses) {
    }

    public void setMinimumNumberOfOptionalCourses(Integer minimumNumberOfOptionalCourses) {
    }

    public String toString() {
        return "minimumCredits[" + super.getMinimumValue() + "] maximumCredits["
                + super.getMaximumValue() + "] branch[" + getBranch().getName() + "]";
    }

	public void delete() throws DomainException {
		if (!hasAnyCurricularCourses()) {
			getScientificAreasForThis().clear();
			super.delete();
		} else {
			throw new DomainException(this.getClass().getName(), "ola mundo");			
		}
	}
}