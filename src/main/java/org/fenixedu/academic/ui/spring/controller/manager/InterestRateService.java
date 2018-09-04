package org.fenixedu.academic.ui.spring.controller.manager;

import org.fenixedu.academic.domain.InterestRateLog;
import org.fenixedu.academic.domain.accounting.InterestRate;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.Atomic;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.fenixedu.academic.domain.accounting.InterestRate.getOrCreate;

@Service
public class InterestRateService {

    public List<InterestRate> getAllInterestRates() {
        return Bennu.getInstance().getInterestRateSet().stream().sorted(Comparator.comparing(InterestRate::getStart).
                reversed()).collect(Collectors.toList());
    }

    public List<InterestRateLog> getAllInterestRatesLogs() {
        return Bennu.getInstance().getInterestRateLogSet().stream().sorted(InterestRateLog.COMPARATOR_BY_WHEN_DATETIME).
                collect(Collectors.toList());
    }

    @Atomic
    public InterestRate createInterestRate(LocalDate startDate, LocalDate endDate, BigDecimal value) throws FenixActionException {
        return getOrCreate(startDate, endDate, value);
    }

    @Atomic
    public void editInterestRate(InterestRate interestRate, LocalDate startDate, LocalDate endDate, BigDecimal value) throws FenixActionException {
        interestRate.editIfValid(startDate, endDate, value);
    }

}
