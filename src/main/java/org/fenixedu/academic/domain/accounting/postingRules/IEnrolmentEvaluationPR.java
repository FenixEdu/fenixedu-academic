package org.fenixedu.academic.domain.accounting.postingRules;

import org.fenixedu.academic.util.Money;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public interface IEnrolmentEvaluationPR {
    Money getFixedAmount();
    Money getFixedAmountPenalty();
}
