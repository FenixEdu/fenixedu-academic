package net.sourceforge.fenixedu.presentationTier.renderers.providers.personnelSection.payrollSection.bonus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection.AnualBonusInstallmentBean;
import net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection.AnualBonusInstallmentFactory;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class InstallmentYearMonthAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	AnualBonusInstallmentBean anualBonusInstallmentBean = (AnualBonusInstallmentBean) source;
	List<YearMonth> yearMonths = new ArrayList<YearMonth>();
	if ((anualBonusInstallmentBean.getYear() - 1) >= AnualBonusInstallmentFactory.firstYear) {
	    yearMonths.add(new YearMonth(anualBonusInstallmentBean.getYear() - 1, Month.DECEMBER));
	}
	for (Month month : Month.values()) {
	    yearMonths.add(new YearMonth(anualBonusInstallmentBean.getYear(), month));
	}
	ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator("year"));
	comparatorChain.addComparator(new BeanComparator("month"));
	Collections.sort(yearMonths, comparatorChain);
	return yearMonths;
    }

    public Converter getConverter() {
	return null;
    }
}
