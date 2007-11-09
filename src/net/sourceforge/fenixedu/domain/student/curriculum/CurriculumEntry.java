package net.sourceforge.fenixedu.domain.student.curriculum;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.lang.StringUtils;

abstract public class CurriculumEntry implements Serializable, ICurriculumEntry {

    public boolean isEnrolmentEntry() {
	return false;
    }

    final public boolean getIsEnrolmentEntry() {
	return isEnrolmentEntry();
    }

    public boolean isNotNeedToEnrolEntry() {
	return false;
    }

    final public boolean getIsNotNeedToEnrolEntry() {
	return isNotNeedToEnrolEntry();
    }

    public boolean isEquivalentEnrolmentEntry() {
	return false;
    }

    final public boolean getIsEquivalentEnrolmentEntry() {
	return isEquivalentEnrolmentEntry();
    }

    public boolean isNotInDegreeCurriculumEnrolmentEntry() {
	return false;
    }

    final public boolean getIsNotInDegreeCurriculumEnrolmentEntry() {
	return isNotInDegreeCurriculumEnrolmentEntry();
    }

    public boolean isDismissalEntry() {
	return false;
    }

    final public boolean getIsDismissalEntry() {
	return isDismissalEntry();
    }

    protected double ectsCredits(final CurricularCourse curricularCourse) {
	final double ectsCredits = curricularCourse.getEctsCredits().doubleValue();
	return ectsCredits == 0 ? 6.0 : ectsCredits;
    }

    public BigDecimal getWeigthTimesGrade() {
	final String grade = getGradeValue();
	return StringUtils.isNumeric(grade) ? getWeigthForCurriculum().multiply(BigDecimal.valueOf(Double.valueOf(grade))) : null;
    }

    abstract public Grade getGrade();

    public String getGradeValue() {
	return getGrade() == null ? null :getGrade().getValue();
    }

    public ExecutionPeriod getExecutionPeriod() {
	return null;
    }

    final public ExecutionYear getExecutionYear() {
	return getExecutionPeriod() == null ? null : getExecutionPeriod().getExecutionYear();
    }

    final public String getCode() {
	return null;
    }

    final public MultiLanguageString getName() {
	return new MultiLanguageString();
    }

}
