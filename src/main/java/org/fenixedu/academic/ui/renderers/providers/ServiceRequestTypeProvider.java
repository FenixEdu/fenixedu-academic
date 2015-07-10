package org.fenixedu.academic.ui.renderers.providers;

import java.util.stream.Collectors;

import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ServiceRequestTypeProvider implements DataProvider {

    public static class DeclarationsProvider extends ServiceRequestTypeProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            return ServiceRequestType.findDeclarations().sorted().collect(Collectors.toList());
        }
    }

    public static class CertificatesProvider extends ServiceRequestTypeProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            return ServiceRequestType.findCertificates().sorted().collect(Collectors.toList());
        }
    }

    public static class ServicesProvider extends ServiceRequestTypeProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            return ServiceRequestType.findServices().sorted().collect(Collectors.toList());
        }
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        return ServiceRequestType.findActive().sorted().collect(Collectors.toList());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
