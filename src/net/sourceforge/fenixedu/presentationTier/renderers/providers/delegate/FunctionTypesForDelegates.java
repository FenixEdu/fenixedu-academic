package net.sourceforge.fenixedu.presentationTier.renderers.providers.delegate;

import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class FunctionTypesForDelegates implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return FunctionType.getAllDelegateFunctionTypes();
    }

    public Converter getConverter() {
	return new Converter() {

	    @Override
	    public Object convert(Class type, Object value) {
		return FunctionType.valueOf((String) value);
	    }

	};
    }
}
