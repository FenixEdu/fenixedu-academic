package net.sourceforge.fenixedu.presentationTier.renderers.providers.credits;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.SharedFunction;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SharedFunctionsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	PersonFunctionBean personFunctionBean = (PersonFunctionBean) source;
	List<SharedFunction> functions = new ArrayList<SharedFunction>();
	if (personFunctionBean.getUnit() != null) {
	    for (Function function : personFunctionBean.getUnit().getFunctions(true)) {
		if (function instanceof SharedFunction) {
		    functions.add((SharedFunction) function);
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
