package net.sourceforge.fenixedu.presentationTier.renderers.providers.personnelSection.payrollSection.bonus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.IntegerNumberConverter;

public class AnualInstallmentYearAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	List<Integer> years = new ArrayList<Integer>();
	for (AnualBonusInstallment anualBonusInstallment : RootDomainObject.getInstance().getAnualBonusInstallments()) {
	    if (!years.contains(anualBonusInstallment.getYear())) {
		years.add(anualBonusInstallment.getYear());
	    }
	}
	Collections.sort(years);
	return years;
    }

    public Converter getConverter() {
	return new IntegerNumberConverter();
    }

}
