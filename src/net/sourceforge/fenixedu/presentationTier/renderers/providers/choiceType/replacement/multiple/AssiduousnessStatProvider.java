package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumArrayConverter;

public class AssiduousnessStatProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(AssiduousnessState.values());
    }

    @Override
    public Converter getConverter() {
	return new EnumArrayConverter(AssiduousnessState.class);
    }

}
