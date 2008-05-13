package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class AssiduousnessStatusAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return RootDomainObject.getInstance().getAssiduousnessStatus();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
