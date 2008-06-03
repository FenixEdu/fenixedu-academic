package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class FunctionProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	if (source instanceof ProtocolFactory) {
	    ProtocolFactory protocolFactory = (ProtocolFactory) source;
	    if (!protocolFactory.getFunctionByPerson()) {
		return protocolFactory.getResponsibleFunctionUnit().getFunctions();
	    } else {
		List<Function> functions = new ArrayList<Function>();
		for (PersonFunction accountability : protocolFactory.getResponsible().getPerson().getActivePersonFunctions()) {
		    functions.add(accountability.getFunction());
		}
		return functions;
	    }
	}
	return null;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
