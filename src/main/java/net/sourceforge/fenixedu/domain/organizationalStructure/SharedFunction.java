package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void setCredits(BigDecimal credits) {
        super.setCredits(credits);
        for (PersonFunctionShared personFunctionShared : getPersonFunctionsShared()) {
            personFunctionShared.recalculateCredits();
        }
    }

    public List<PersonFunctionShared> getPersonFunctionsShared() {
        List<PersonFunctionShared> personFunctions = new ArrayList<PersonFunctionShared>();
        for (Accountability accountability : getAccountabilities()) {
            if (accountability.isPersonFunctionShared()) {
                personFunctions.add((PersonFunctionShared) accountability);
            }
        }
        return personFunctions;
    }
    @Deprecated
    public boolean hasCredits() {
        return getCredits() != null;
    }

}
