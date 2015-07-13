package org.fenixedu.academic.domain.serviceRequests;

import java.util.stream.Stream;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class ServiceRequestType extends ServiceRequestType_Base {

    protected ServiceRequestType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected ServiceRequestType(final String code, final LocalizedString name, final boolean payed) {
        this();

        super.setCode(code);
        super.setName(name);
        setPayed(payed);

        checkRules();
    }

    protected ServiceRequestType(final String code, final LocalizedString name,
            final AcademicServiceRequestType academicServiceRequestType, final DocumentRequestType documentRequestType,
            final boolean payed) {
        this(code, name, payed);
        setAcademicServiceRequestType(academicServiceRequestType);
        setDocumentRequestType(documentRequestType);

        checkRules();
    }

    private void checkRules() {

    }

    public boolean isPayed() {
        return getPayed();
    }

    public boolean hasOption(final ServiceRequestTypeOption option) {
        return false;
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final boolean payed) {
        setCode(code);
        setName(name);
        setPayed(payed);

        checkRules();
    }

    public boolean isOptionAssociated(final ServiceRequestTypeOption option) {
        return getServiceRequestTypeOptionsSet().contains(option);
    }

    @Atomic
    public void associateOption(ServiceRequestTypeOption serviceRequestTypeOption) {
        addServiceRequestTypeOptions(serviceRequestTypeOption);

        checkRules();
    }

    @Atomic
    public void removeOption(final ServiceRequestTypeOption option) {
        removeServiceRequestTypeOptions(option);

        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new DomainException("error.ServiceRequestType.delete.not.possible");
        }

        getServiceRequestTypeOptionsSet().clear();

        setRootDomainObject(null);

        super.deleteDomainObject();
    }

    /*---------
     * SERVICES
     * --------
     */

    public static Stream<ServiceRequestType> findAll() {
        return Bennu.getInstance().getServiceRequestTypesSet().stream();
    }

    public static ServiceRequestType findUnique(final AcademicServiceRequestType academicServiceRequestType) {
        return null;
    }

    public static ServiceRequestType findUnique(final AcademicServiceRequestType academicServiceRequestType,
            final DocumentRequestType documentRequestType) {
        return findAll()
                .filter(s -> s.getAcademicServiceRequestType() == academicServiceRequestType
                        && s.getDocumentRequestType() == documentRequestType).findFirst().orElse(null);
    }

    public static ServiceRequestType findUnique(final AcademicServiceRequest academicServiceRequest) {
        if (academicServiceRequest.getServiceRequestType() != null) {
            return academicServiceRequest.getServiceRequestType();
        }

        // Fallback 
        if (academicServiceRequest.isDocumentRequest()) {
            return findUnique(academicServiceRequest.getAcademicServiceRequestType(),
                    ((DocumentRequest) academicServiceRequest).getDocumentRequestType());
        } else {
            return findUnique(academicServiceRequest.getAcademicServiceRequestType());
        }
    }

    @Atomic
    public static ServiceRequestType create(final String code, final LocalizedString name, final boolean payed) {
        return new ServiceRequestType(code, name, payed);
    }

    @Atomic
    public static ServiceRequestType createLegacy(final String code, final LocalizedString name,
            final AcademicServiceRequestType academicServiceRequestType, final DocumentRequestType documentRequestType,
            final boolean payed) {
        return new ServiceRequestType(code, name, academicServiceRequestType, documentRequestType, payed);
    }

}
