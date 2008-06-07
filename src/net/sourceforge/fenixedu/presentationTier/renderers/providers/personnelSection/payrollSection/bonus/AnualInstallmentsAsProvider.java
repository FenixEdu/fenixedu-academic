package net.sourceforge.fenixedu.presentationTier.renderers.providers.personnelSection.payrollSection.bonus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection.BonusInstallment;
import net.sourceforge.fenixedu.dataTransferObject.personnelSection.payrollSection.BonusInstallmentFileBean;
import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.AnualBonusInstallment;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.IntegerNumberConverter;

public class AnualInstallmentsAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	int year = new YearMonthDay().getYear();
	if (source instanceof BonusInstallmentFileBean) {
	    BonusInstallmentFileBean bonusInstallmentFileBean = (BonusInstallmentFileBean) source;
	    year = bonusInstallmentFileBean.getYear();
	} else {
	    BonusInstallment bonusInstallment = (BonusInstallment) source;
	    year = bonusInstallment.getYear();
	}
	int installmentsTotalNumber = AnualBonusInstallment.readByYear(year).size();
	List<Integer> installments = new ArrayList<Integer>();
	for (int installmentNumber = 1; installmentNumber <= installmentsTotalNumber; installmentNumber++) {
	    installments.add(installmentNumber);
	}
	Collections.sort(installments);
	return installments;
    }

    public Converter getConverter() {
	return new IntegerNumberConverter();
    }

}
