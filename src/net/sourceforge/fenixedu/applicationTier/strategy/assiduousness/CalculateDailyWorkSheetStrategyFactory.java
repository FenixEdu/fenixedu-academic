package net.sourceforge.fenixedu.applicationTier.strategy.assiduousness;

import net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.strategys.CalculateDailyWorkSheetWithUnjustifiedDaysStategy;
import net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.strategys.CalculateDailyWorkSheetWithoutUnjustifiedDaysStategy;
import net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.strategys.ICalculateDailyWorkSheetStrategy;

import org.joda.time.LocalDate;

public class CalculateDailyWorkSheetStrategyFactory {
    private static final LocalDate UNJUSTIFIED_DAYS_RULE_DATE = new LocalDate(2009, 07, 01);
    private static CalculateDailyWorkSheetStrategyFactory instance = null;

    private CalculateDailyWorkSheetStrategyFactory() {
    }

    public static synchronized CalculateDailyWorkSheetStrategyFactory getInstance() {
	if (instance == null) {
	    instance = new CalculateDailyWorkSheetStrategyFactory();
	}
	return instance;
    }

    public ICalculateDailyWorkSheetStrategy getCalculateDailyWorkSheetStrategy(final LocalDate date) {
	if (date.isBefore(UNJUSTIFIED_DAYS_RULE_DATE)) {
	    return new CalculateDailyWorkSheetWithoutUnjustifiedDaysStategy();
	}
	return new CalculateDailyWorkSheetWithUnjustifiedDaysStategy();
    }
}