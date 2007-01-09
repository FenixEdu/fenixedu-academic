package net.sourceforge.fenixedu.domain.student.curriculum;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;

public abstract class CurriculumEntry implements Serializable {

	public abstract double getEctsCredits();

	public abstract double getWeigth();
	
	public abstract Double getWeigthTimesClassification();

	public boolean isEnrolmentEntry() {
	    return false;
	}
	public boolean getIsEnrolmentEntry() {
	    return isEnrolmentEntry();
	}
	public boolean isNotNeedToEnrolEntry() {
	    return false;
	}
	public boolean getIsNotNeedToEnrolEntry() {
	    return isNotNeedToEnrolEntry();
	}
	public boolean isEquivalentEnrolmentEntry() {
	    return false;
	}
	public boolean getIsEquivalentEnrolmentEntry() {
	    return isEquivalentEnrolmentEntry();
	}
	public boolean isNotInDegreeCurriculumEnrolmentEntry() {
	    return false;
	}
	public boolean getIsNotInDegreeCurriculumEnrolmentEntry() {
	    return isNotInDegreeCurriculumEnrolmentEntry();
	}

	protected double ectsCredits(final CurricularCourse curricularCourse) {
	    final double ectsCredits = curricularCourse.getEctsCredits().doubleValue();
	    return ectsCredits == 0 ? 6.0 : ectsCredits;
	}

}
