package net.sourceforge.fenixedu.presentationTier.renderers.providers.serviceRequests;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class AcademicServiceRequestSituationTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return Arrays.asList(AcademicServiceRequestSituationType.values());
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
