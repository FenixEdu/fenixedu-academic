package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class JustificationMotiveAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        EmployeeJustificationFactory employeeJustificationFactory = (EmployeeJustificationFactory) source;
        return employeeJustificationFactory.getJustificationMotivesList();
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
