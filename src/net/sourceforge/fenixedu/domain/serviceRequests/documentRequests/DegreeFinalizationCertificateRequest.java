package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base {

    protected DegreeFinalizationCertificateRequest() {
        super();
    }

    public DegreeFinalizationCertificateRequest(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
            String otherDocumentPurposeTypeDescription, Integer numberOfPages, Boolean urgentRequest,
            Boolean average) {
        this();
        init(studentCurricularPlan, administrativeOffice, documentPurposeType,
                otherDocumentPurposeTypeDescription, numberOfPages, urgentRequest, average);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
            String otherDocumentPurposeTypeDescription, Integer numberOfPages, Boolean urgentRequest,
            Boolean average) {
        init(studentCurricularPlan, administrativeOffice, documentPurposeType,
                otherDocumentPurposeTypeDescription, numberOfPages, urgentRequest);

        checkParameters(average);
        super.setAverage(average);

    }

    private void checkParameters(Boolean average) {
        if (average == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.average.cannot.be.null");
        }
    }

    @Override
    public void setAverage(Boolean average) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.cannot.modify.average");
    }

}
