package net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests;

import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.documents.DocumentRequestGeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.AdministrativeOfficeDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

public abstract class PhdDocumentRequest extends PhdDocumentRequest_Base implements IDocumentRequest {

    protected PhdDocumentRequest() {
        super();
    }

    @Override
    protected void init(PhdAcademicServiceRequestCreateBean bean) {
        throw new DomainException("invoke init(PhdAcademicServiceRequestCreateBean)");
    }

    protected void init(PhdDocumentRequestCreateBean bean) {
        super.init(bean);
    }

    @Override
    public String getDescription() {
        return getDescription(getAcademicServiceRequestType(), getDocumentRequestType().getQualifiedName());
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.DOCUMENT;
    }

    @Override
    abstract public DocumentRequestType getDocumentRequestType();

    @Override
    abstract public String getDocumentTemplateKey();

    @Override
    final public boolean isDiploma() {
        return getDocumentRequestType().isDiploma();
    }

    @Override
    public boolean isRegistryDiploma() {
        return getDocumentRequestType().isRegistryDiploma();
    }

    @Override
    final public boolean isDiplomaSupplement() {
        return getDocumentRequestType().isDiplomaSupplement();
    }

    @Override
    public boolean isCertificate() {
        return false;
    }

    @Override
    public boolean isToShowCredits() {
        return false;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToProcess()) {
            if (!getFreeProcessed()) {
                assertPayedEvents();
            }
        }
    }

    protected void assertPayedEvents() {
        if (getPhdIndividualProgramProcess().hasInsuranceDebtsCurrently()) {
            throw new PhdDomainOperationException("DocumentRequest.registration.has.not.payed.insurance.fees");
        }

        if (getPhdIndividualProgramProcess().hasAdministrativeOfficeFeeAndInsuranceDebtsCurrently(getAdministrativeOffice())) {
            throw new PhdDomainOperationException("DocumentRequest.registration.has.not.payed.administrative.office.fees");
        }
    }

    protected void assertPayedEvents(final ExecutionYear executionYear) {
        if (executionYear != null) {
            if (getPhdIndividualProgramProcess().hasInsuranceDebts(executionYear)) {
                throw new PhdDomainOperationException("DocumentRequest.registration.has.not.payed.insurance.fees");
            }

            if (getPhdIndividualProgramProcess().hasAdministrativeOfficeFeeAndInsuranceDebts(getAdministrativeOffice(),
                    executionYear)) {
                throw new PhdDomainOperationException("DocumentRequest.registration.has.not.payed.administrative.office.fees");
            }
        }
    }

    @Override
    public boolean isDownloadPossible() {
        return getLastGeneratedDocument() != null;
    }

    public boolean hasNumberOfPages() {
        return getNumberOfPages() != null && getNumberOfPages().intValue() != 0;
    }

    @Override
    public byte[] generateDocument() {
        try {
            final List<AdministrativeOfficeDocument> documents =
                    AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(this);
            final AdministrativeOfficeDocument[] array = {};
            byte[] data = ReportsUtils.exportMultipleToPdfAsByteArray(documents.toArray(array));
            DocumentRequestGeneratedDocument.store(this, documents.iterator().next().getReportFileName() + ".pdf", data);
            return data;
        } catch (JRException e) {
            e.printStackTrace();
            throw new DomainException("error.documentRequest.errorGeneratingDocument");
        }
    }

    @Override
    public String getReportFileName() {
        return AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(this).iterator().next()
                .getReportFileName();
    }

}
