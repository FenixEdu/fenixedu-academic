package net.sourceforge.fenixedu.presentationTier.renderers.providers.accounting.events;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.accounting.events.AccountingEventCreateBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionPeriodsForAccountingEventCreationProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final AccountingEventCreateBean accountingEventCreateBean = (AccountingEventCreateBean) source;

	if (accountingEventCreateBean.getExecutionYear() != null) {
	    return accountingEventCreateBean.getExecutionYear().getExecutionPeriods();
	}

	return Collections.EMPTY_LIST;

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
