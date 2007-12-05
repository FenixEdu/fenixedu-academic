package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class FunctionProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	if (source instanceof ProtocolFactory) {
	    return ((ProtocolFactory) source).getResponsibleFunctionUnit().getFunctions();
	}
	return null;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
