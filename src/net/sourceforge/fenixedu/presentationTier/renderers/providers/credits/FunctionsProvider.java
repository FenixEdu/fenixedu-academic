package net.sourceforge.fenixedu.presentationTier.renderers.providers.credits;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class FunctionsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	PersonFunctionBean personFunctionBean = (PersonFunctionBean) source;
	List<Function> functions = new ArrayList<Function>();
	if (personFunctionBean.getUnit() != null) {
	    for (Function function : personFunctionBean.getUnit().getFunctions(true)) {
		if (function instanceof Function) {
		    functions.add(function);
		}
	    }
	}
	return functions;
    }

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
