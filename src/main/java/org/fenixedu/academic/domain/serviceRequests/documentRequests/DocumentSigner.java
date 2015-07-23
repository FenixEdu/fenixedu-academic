package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.Comparator;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class DocumentSigner extends DocumentSigner_Base {

    final static public Comparator<DocumentSigner> DEFAULT_COMPARATOR = new Comparator<DocumentSigner>() {

        @Override
        public int compare(DocumentSigner ds1, DocumentSigner ds2) {
            if (ds1.isDefaultSignature()) {
                return -1;
            }
            if (ds2.isDefaultSignature()) {
                return 1;
            }
            return ds1.getResponsibleName().compareTo(ds2.getResponsibleName());
        }

    };

    protected DocumentSigner() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected DocumentSigner(AdministrativeOffice administrativeOffice, String responsibleName,
            LocalizedString responsibleFunction, LocalizedString responsibleUnit, Gender responsibleGender) {
        this();
        setAdministrativeOffice(administrativeOffice);
        setResponsibleName(responsibleName);
        setResponsibleFunction(responsibleFunction);
        setResponsibleUnit(responsibleUnit);
        setResponsibleGender(responsibleGender);

        if (findDefaultDocumentSignature() == null) {
            setDefaultSignature(true);
        }
    }

    private void checkRules() {
    }

    @Atomic
    public void edit(String responsibleName, LocalizedString responsibleFunction, LocalizedString responsibleUnit,
            Gender responsibleGender) {
        setResponsibleName(responsibleName);
        setResponsibleFunction(responsibleFunction);
        setResponsibleUnit(responsibleUnit);
        setResponsibleGender(responsibleGender);
    }

    public boolean isDeletable() {
        return getAcademicServiceRequestsSet().stream().filter(AcademicServiceRequest::finishedSuccessfully).count() == 0;
    }

    public void delete() {
        if (!isDeletable()) {
            throw new DomainException(
                    "serviceRequests.documentRequests.DocumentSigner.cant.delete.signer.associated.with.concluded.or.delivered.documents");
        }
        DocumentSigner defaultSigner = findDefaultDocumentSignature();
        for (AcademicServiceRequest asr : getAcademicServiceRequestsSet()) {
            asr.setDocumentSigner(defaultSigner != this ? defaultSigner : null);
        }
        setBennu(null);
        setAdministrativeOffice(null);
        deleteDomainObject();
    }

    public boolean isDefaultSignature() {
        return getDefaultSignature() != null ? getDefaultSignature() : false;
    }

    @Override
    public void setDefaultSignature(Boolean defaultSignature) {
        if (defaultSignature) {
            DocumentSigner defaultDocumentSignature = findDefaultDocumentSignature();
            if (defaultDocumentSignature != null) {
                defaultDocumentSignature.setDefaultSignature(false);
            }
        }
        super.setDefaultSignature(defaultSignature);
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<DocumentSigner> findAll() {
        return Bennu.getInstance().getDocumentSignersSet().stream();
    }

    public static DocumentSigner findDefaultDocumentSignature() {
        return findAll().filter(DocumentSigner::isDefaultSignature).findFirst().orElse(null);
    }

    @Atomic
    public static DocumentSigner create(AdministrativeOffice administrativeOffice, String responsibleName,
            LocalizedString responsibleFunction, LocalizedString responsibleUnit, Gender responsibleGender) {
        return new DocumentSigner(administrativeOffice, responsibleName, responsibleFunction, responsibleUnit, responsibleGender);
    }

}
