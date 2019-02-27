package org.fenixedu.academic.domain.accounting.postingRules;

import java.util.Optional;

import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.util.Money;
import org.joda.time.LocalDate;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public interface IEnrolmentEvaluationPR {
    Money getFixedAmount();
    Money getFixedAmountPenalty();
    Optional<LocalDate> getDueDate(Event event);
}
