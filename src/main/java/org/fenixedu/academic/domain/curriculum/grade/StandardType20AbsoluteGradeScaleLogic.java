package org.fenixedu.academic.domain.curriculum.grade;

import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScaleEnum.GradeScaleLogic;

@Deprecated
public class StandardType20AbsoluteGradeScaleLogic extends StandardType20GradeScaleLogic implements GradeScaleLogic {

    @Override
    public boolean isNotApproved(final Grade grade) {
        final String value = grade.getValue();

        if (NOT_APPROVED_TEXTUAL_GRADES.contains(value) || isNotEvaluated(grade)) {
            return true;
        }

        try {
            return Double.parseDouble(value) < 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean isApproved(final Grade grade) {
        final String value = grade.getValue();

        if (APPROVED_TEXTUAL_GRADES.contains(value)) {
            return true;
        }

        try {
            final double doubleValue = Double.parseDouble(value);
            return 0 <= doubleValue && doubleValue <= 20;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
