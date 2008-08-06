package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import net.sourceforge.fenixedu.dataTransferObject.accounting.DepositAmountBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class EntryTypeForDepositProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final DepositAmountBean depositAmountBean = (DepositAmountBean) source;

	return depositAmountBean.getEvent().getPossibleEntryTypesForDeposit();

    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
