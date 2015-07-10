package org.fenixedu.academic.ui.renderers.providers;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ServiceRequestTypeProvider implements DataProvider {

    public static class DeclarationsProvider extends ServiceRequestTypeProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            return ServiceRequestType.findDeclarations().filter(ServiceRequestType::isActive)
                    .sorted(Comparator.comparing(ServiceRequestType::getName)).collect(Collectors.toList());
        }
    }

    public static class CertificatesProvider extends ServiceRequestTypeProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            return ServiceRequestType.findCertificates().filter(ServiceRequestType::isActive)
                    .sorted(Comparator.comparing(ServiceRequestType::getName)).collect(Collectors.toList());
        }
    }

    public static class ServicesProvider extends ServiceRequestTypeProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            return ServiceRequestType.findServices().filter(ServiceRequestType::isActive)
                    .sorted(Comparator.comparing(ServiceRequestType::getName)).collect(Collectors.toList());
        }
    }

    public static class PreBolognaCertificatesProvider extends ServiceRequestTypeProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            return ServiceRequestType.findCertificates().sorted(Comparator.comparing(ServiceRequestType::getName))
                    .collect(Collectors.toList());
        }
    }

    public static class StudentIssuedProvider extends ServiceRequestTypeProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            return ServiceRequestType.findActive()
                    .filter(srt -> srt.getDocumentRequestType() != null && srt.getDocumentRequestType().isStudentRequestable())
                    .sorted(Comparator.comparing(ServiceRequestType::getName)).collect(Collectors.toList());
        }
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        return ServiceRequestType.findActive().sorted(Comparator.comparing(ServiceRequestType::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
