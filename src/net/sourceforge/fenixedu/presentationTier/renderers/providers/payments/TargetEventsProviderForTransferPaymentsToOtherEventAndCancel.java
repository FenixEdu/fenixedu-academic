package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.TransferPaymentsToOtherEventAndCancelBean;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class TargetEventsProviderForTransferPaymentsToOtherEventAndCancel implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final TransferPaymentsToOtherEventAndCancelBean transferPaymentsBetweenEventsBean = (TransferPaymentsToOtherEventAndCancelBean) source;
	final Set<Event> result = new HashSet<Event>();
	result.addAll(transferPaymentsBetweenEventsBean.getSourceEvent().getPerson().getEvents());
	result.remove(transferPaymentsBetweenEventsBean.getSourceEvent());

	return result;

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
