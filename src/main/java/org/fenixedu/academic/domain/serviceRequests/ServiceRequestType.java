package org.fenixedu.academic.domain.serviceRequests;

import java.util.Collection;
import java.util.Comparator;
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

    static final public Comparator<ServiceRequestType> COMPARE_BY_CATEGORY_THEN_BY_NAME = new Comparator<ServiceRequestType>() {

        @Override
        public int compare(ServiceRequestType o1, ServiceRequestType o2) {
            if (o1.getServiceRequestCategory() == null) {
                return -1;
            }
            if (o2.getServiceRequestCategory() == null) {
                return 1;
            }
            final int c = o1.getServiceRequestCategory().compareTo(o2.getServiceRequestCategory());
            return c == 0 ? o1.getName().getContent().compareTo(o2.getName().getContent()) : c;
        }

    };

    protected ServiceRequestType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected ServiceRequestType(final String code, final LocalizedString name, final boolean active, final boolean payable,
            final Boolean notifyUponConclusion, final Boolean printable, final Boolean requestedOnline,
            final ServiceRequestCategory category) {
        this();

        super.setCode(code);
        super.setName(name);
        setActive(active);
        setPayable(payable);
        setNotifyUponConclusion(notifyUponConclusion);
        setPrintable(printable);
        setRequestedOnline(requestedOnline);
        setServiceRequestCategory(category);

        checkRules();
    }

    protected ServiceRequestType(final String code, final LocalizedString name, final boolean active,
            final AcademicServiceRequestType academicServiceRequestType, final DocumentRequestType documentRequestType,
            final boolean payable, final Boolean notifyUponConclusion, final Boolean printable, final Boolean requestedOnline,
            final ServiceRequestCategory category) {
        this(code, name, active, payable, notifyUponConclusion, printable, requestedOnline, category);
        setAcademicServiceRequestType(academicServiceRequestType);
        setDocumentRequestType(documentRequestType);

        checkRules();
    }

    private void checkRules() {
        if (getCode().trim().isEmpty()) {
            throw new DomainException("error.ServiceRequestType.code.empty");
        }
        if (getName().isEmpty()) {
            throw new DomainException("error.ServiceRequestType.name.empty");
        }
        if (getServiceRequestCategory() == null) {
            throw new DomainException("error.ServiceRequestType.category.empty");
        }
        if (findByCode(getCode()).count() > 1) {
            throw new DomainException("error.ServiceRequestType.code.duplicated");
        }
    }

    public boolean isActive() {
        return getActive();
    }

    public boolean isPayable() {
        return getPayable();
    }

    public boolean isToNotifyUponConclusion() {
        return getNotifyUponConclusion() == null ? false : getNotifyUponConclusion();
    }

    public boolean isPrintable() {
        return getPrintable() == null ? false : getPrintable();
    }

    public boolean isRequestedOnline() {
        return getRequestedOnline() == null ? false : getRequestedOnline();
    }

    public boolean isLegacy() {
        return getAcademicServiceRequestType() != null;
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final boolean active, final boolean payable,
            final Boolean notifyUponConclusion, final Boolean printable, final Boolean requestedOnline,
            final ServiceRequestCategory category, final LocalizedString numberOfUnitsLabel) {
        setCode(code);
        setName(name);
        setActive(active);
        setPayable(payable);
        setNotifyUponConclusion(notifyUponConclusion);
        setPrintable(printable);
        setRequestedOnline(requestedOnline);
        setServiceRequestCategory(category);
        setNumberOfUnitsLabel(null);

        checkRules();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        if (getAcademicServiceRequestsSet().size() != 1) {
            blockers.add(
                    BundleUtil.getString(Bundle.APPLICATION, "error.ServiceRequestType.academicServiceRequestsSet.not.empty"));
        }
        super.checkForDeletionBlockers(blockers);
    }

    @Atomic
    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

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
        return findAll().filter(s -> s.getAcademicServiceRequestType() == academicServiceRequestType
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
            final boolean payable, final Boolean notifyUponConclusion, final Boolean printable, final Boolean requestedOnline,
            final ServiceRequestCategory category) {
        return new ServiceRequestType(code, name, active, payable, notifyUponConclusion, printable, requestedOnline, category);
    }

    @Atomic
    public static ServiceRequestType createLegacy(final String code, final LocalizedString name, final boolean active,
            final AcademicServiceRequestType academicServiceRequestType, final DocumentRequestType documentRequestType,
            final boolean payable, final Boolean notifyUponConclusion, final Boolean printable, final Boolean requestedOnline,
            final ServiceRequestCategory category) {
        return new ServiceRequestType(code, name, active, academicServiceRequestType, documentRequestType, payable,
                notifyUponConclusion, printable, requestedOnline, category);
    }

    public String getRichName() {
        return getName().getContent() + " ("
                + BundleUtil.getString(Bundle.STUDENT,
                        isPayable() ? "label.student.serviceRequestTypes.withFees" : "label.student.serviceRequestTypes.noFees")
                + ")";
    }

}
