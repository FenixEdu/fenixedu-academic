package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ExtraWorkRequestFactory;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class EmployeeExtraWorkRequestFactoryAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ExtraWorkRequestFactory extraWorkRequestFactory = (ExtraWorkRequestFactory) source;
        return extraWorkRequestFactory.getEmployeeList();
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
