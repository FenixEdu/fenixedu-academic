package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single;

import java.util.Arrays;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;
import pt.ist.fenixframework.pstm.TransactionAction;

public class TransactionActionProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(TransactionAction.values());
    }

    @Override
    public Converter getConverter() {
	return new EnumConverter();
    }

}
