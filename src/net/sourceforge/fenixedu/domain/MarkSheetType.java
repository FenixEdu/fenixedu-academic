/*
 * Created on Apr 19, 2006
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

public enum MarkSheetType {

    NORMAL(EnrolmentEvaluationType.NORMAL),

    IMPROVEMENT(EnrolmentEvaluationType.IMPROVEMENT),

    SPECIAL_SEASON(EnrolmentEvaluationType.SPECIAL_SEASON),

    SPECIAL_AUTHORIZATION(EnrolmentEvaluationType.NORMAL);

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private MarkSheetType(EnrolmentEvaluationType enrolmentEvaluationType) {
	this.enrolmentEvaluationType = enrolmentEvaluationType;
    }

    public String getName() {
	return name();
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
	return this.enrolmentEvaluationType;
    }
}
