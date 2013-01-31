package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.pstm.TransactionReport;

public class TransactionServersProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final TransactionReport transactionReport = (TransactionReport) source;
		return transactionReport.getServers();
	}

	@Override
	public Converter getConverter() {
		return null;
	}

}
