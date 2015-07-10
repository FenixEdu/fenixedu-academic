package org.fenixedu.academic.domain.serviceRequests;

public enum ServiceRequestCategory {
    DECLARATIONS, CERTIFICATES, SERVICES;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return ServiceRequestCategory.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return ServiceRequestCategory.class.getName() + "." + name();
    }
}
