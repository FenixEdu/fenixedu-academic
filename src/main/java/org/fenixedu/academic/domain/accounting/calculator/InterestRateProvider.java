package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;
import java.util.Optional;

import org.joda.time.LocalDate;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public interface InterestRateProvider {
    Optional<InterestRateBean> getInterestRateBean(LocalDate start, LocalDate end, BigDecimal baseAmount);
}
