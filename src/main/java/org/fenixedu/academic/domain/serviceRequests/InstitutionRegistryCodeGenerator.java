package org.fenixedu.academic.domain.serviceRequests;

import org.fenixedu.bennu.core.domain.Bennu;

public class InstitutionRegistryCodeGenerator extends InstitutionRegistryCodeGenerator_Base {

    public InstitutionRegistryCodeGenerator() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setNumber(0);
    }

    public String getCode(AcademicServiceRequest request) {
        setNumber(getNumber() + 1);
        return getNumber().toString();
    }

    public RegistryCode createRegistryFor(AcademicServiceRequest request) {
        return new RegistryCode(this, request);
    }

}
