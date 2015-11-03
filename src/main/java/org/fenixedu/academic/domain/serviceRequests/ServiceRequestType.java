package org.fenixedu.academic.domain.serviceRequests;

import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class ServiceRequestType extends ServiceRequestType_Base {

    protected ServiceRequestType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected ServiceRequestType(final String code, final LocalizedString name, final boolean active, final boolean payable,
            final ServiceRequestCategory category) {
        this();

        super.setCode(code);
        super.setName(name);
        setActive(active);
        setPayable(payable);
        setServiceRequestCategory(category);

        checkRules();
    }

    protected ServiceRequestType(final String code, final LocalizedString name, final boolean active,
            final AcademicServiceRequestType academicServiceRequestType, final DocumentRequestType documentRequestType,
            final boolean payable, final ServiceRequestCategory category) {
        this(code, name, active, payable, category);
        setAcademicServiceRequestType(academicServiceRequestType);
        setDocumentRequestType(documentRequestType);

        checkRules();
    }

    private void checkRules() {

    }

    public boolean isActive() {
        return getActive();
    }

    public boolean isPayable() {
        return getPayable();
    }

    public boolean isLegacy() {
        return getAcademicServiceRequestType() != null;
    }

    public boolean hasOption(final ServiceRequestTypeOption option) {
        return getServiceRequestTypeOptionsSet().contains(option);
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final boolean active, final boolean payable,
            final ServiceRequestCategory category, final LocalizedString numberOfUnitsLabel) {
        setCode(code);
        setName(name);
        setActive(active);
        setPayable(payable);
        setServiceRequestCategory(category);
        if (hasOption(ServiceRequestTypeOption.findNumberOfUnitsOption().get())) {
            setNumberOfUnitsLabel(numberOfUnitsLabel);
        } else {
            setNumberOfUnitsLabel(null);
        }

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
        return findAll()
                .filter(x -> x.getAcademicServiceRequestType() != null
                        && x.getAcademicServiceRequestType().equals(academicServiceRequestType))
                .filter(x -> x.getDocumentRequestType() == null).findFirst().orElse(null);
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

    public static Stream<ServiceRequestType> findByCode(final String code) {
        return findAll().filter(l -> l.getCode().equalsIgnoreCase(code));
    }

    public static Optional<ServiceRequestType> findUniqueByCode(final String code) {
        return findByCode(code).findFirst();
    }

    public static Stream<ServiceRequestType> findActive() {
        return findAll().filter(ServiceRequestType::isActive);
    }

    public static Stream<ServiceRequestType> findDeclarations() {
        return findAll().filter(srt -> srt.getServiceRequestCategory() == ServiceRequestCategory.DECLARATIONS);
    }

    public static Stream<ServiceRequestType> findCertificates() {
        return findAll().filter(srt -> srt.getServiceRequestCategory() == ServiceRequestCategory.CERTIFICATES);
    }

    public static Stream<ServiceRequestType> findServices() {
        return findAll().filter(srt -> srt.getServiceRequestCategory() == ServiceRequestCategory.SERVICES);
    }

    @Atomic
    public static ServiceRequestType create(final String code, final LocalizedString name, final boolean active,
            final boolean payable, final ServiceRequestCategory category) {
        return new ServiceRequestType(code, name, active, payable, category);
    }

    @Atomic
    public static ServiceRequestType createLegacy(final String code, final LocalizedString name, final boolean active,
            final AcademicServiceRequestType academicServiceRequestType, final DocumentRequestType documentRequestType,
            final boolean payable, final ServiceRequestCategory category) {
        return new ServiceRequestType(code, name, active, academicServiceRequestType, documentRequestType, payable, category);
    }

    public String getRichName() {
        return getName().getContent()
                + " ("
                + BundleUtil
                        .getString(
                                Bundle.STUDENT,
                                (isPayable() ? "label.student.serviceRequestTypes.withFees" : "label.student.serviceRequestTypes.noFees"))
                + ")";
    }

}
