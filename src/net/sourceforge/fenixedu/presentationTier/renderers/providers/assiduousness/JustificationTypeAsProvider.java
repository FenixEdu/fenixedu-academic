package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class JustificationTypeAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return JustificationType.getJustificationTypesForJustificationMotives();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }
}
