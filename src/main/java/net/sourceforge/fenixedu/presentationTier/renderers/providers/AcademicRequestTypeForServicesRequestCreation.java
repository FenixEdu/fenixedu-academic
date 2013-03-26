package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class AcademicRequestTypeForServicesRequestCreation implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return AcademicServiceRequestType.getServiceRequests();
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
