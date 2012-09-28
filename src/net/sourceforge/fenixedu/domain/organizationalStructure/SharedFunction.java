package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SharedFunction extends SharedFunction_Base {

    public SharedFunction(MultiLanguageString functionName, YearMonthDay beginDate, YearMonthDay endDate, FunctionType type,
	    Unit unit, BigDecimal credits) {
	setValues(functionName, beginDate, endDate, type, unit, AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
	setCredits(credits);
    }

    @Override
    public boolean isSharedFunction() {
	return true;
    }

}
