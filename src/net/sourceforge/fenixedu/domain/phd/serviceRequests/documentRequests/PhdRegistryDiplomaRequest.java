package net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.PhdRegistryDiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.documents.DocumentRequestGeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;
import net.sourceforge.fenixedu.domain.serviceRequests.IRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRectorateSubmissionBatchDocumentEntry;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.AdministrativeOfficeDocument;
import net.sourceforge.fenixedu.util.renderer.tools.latex.LatexFontSize;
import net.sourceforge.fenixedu.util.renderer.tools.latex.LatexStringRendererException;
import net.sourceforge.fenixedu.util.renderer.tools.latex.LatexStringRendererService;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class PhdRegistryDiplomaRequest extends PhdRegistryDiplomaRequest_Base implements IRegistryDiplomaRequest,
	IRectorateSubmissionBatchDocumentEntry {

    protected PhdRegistryDiplomaRequest() {
	super();
    }

    protected PhdRegistryDiplomaRequest(final PhdDocumentRequestCreateBean bean) {
	this();
	init(bean);
    }

    @Override
    protected void init(AcademicServiceRequestCreateBean bean) {
	throw new DomainException("invoke init(PhdDocumentRequestCreateBean)");
    }

    @Override
    protected void init(PhdAcademicServiceRequestCreateBean bean) {
	throw new DomainException("invoke init(PhdDocumentRequestCreateBean)");
    }

    protected void init(final PhdDocumentRequestCreateBean bean) {
	checkParameters(bean);
	super.init(bean);

	if (!isFree()) {
	    PhdRegistryDiplomaRequestEvent.create(getAdministrativeOffice(), getPhdIndividualProgramProcess().getPerson(), this);
	}

	setDiplomaSupplement(PhdDiplomaSupplementRequest.create(bean));
    }

    private void checkParameters(final PhdDocumentRequestCreateBean bean) {
	PhdIndividualProgramProcess process = bean.getPhdIndividualProgramProcess();
	if (process.hasRegistryDiplomaRequest()) {
	    throw new PhdDomainOperationException("error.phdRegistryDiploma.alreadyRequested");
	}

	if (!process.isBolonha()) {
	    return;
	}

	if (process.hasDiplomaRequest()) {
	    throw new PhdDomainOperationException("error.phdRegistryDiploma.alreadyHasDiplomaRequest");
	}

    }

    @Override
    public boolean isPayedUponCreation() {
	return true;
    }

    @Override
    public boolean isToPrint() {
	return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
	return true;
    }

    @Override
    public EventType getEventType() {
	return EventType.BOLONHA_PHD_REGISTRY_DIPLOMA_REQUEST;
    }

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

    @Override
    public CycleType getRequestedCycle() {
	return CycleType.THIRD_CYCLE;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.REGISTRY_DIPLOMA_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
	return this.getClass().getName();
    }

    @Override
    public String getFinalAverage(final Locale locale) {
	PhdThesisFinalGrade finalGrade = getPhdIndividualProgramProcess().getFinalGrade();
	return finalGrade.getLocalizedName(locale);
    }

    @Override
    public String getQualifiedAverageGrade(final Locale locale) {
	String qualifiedAverageGrade;

	PhdThesisFinalGrade grade = getPhdIndividualProgramProcess().getFinalGrade();

	switch (grade) {
	case APPROVED:
	case PRE_BOLONHA_APPROVED:
	    qualifiedAverageGrade = "sufficient";
	    break;
	case APPROVED_WITH_PLUS:
	case PRE_BOLONHA_APPROVED_WITH_PLUS:
	    qualifiedAverageGrade = "good";
	    break;
	case APPROVED_WITH_PLUS_PLUS:
	case PRE_BOLONHA_APPROVED_WITH_PLUS_PLUS:
	    qualifiedAverageGrade = "verygood";
	    break;
	default:
	    throw new DomainException("docs.academicAdministrativeOffice.RegistryDiploma.unknown.grade");
	}

	return "diploma.supplement.qualifiedgrade." + qualifiedAverageGrade;
    }

    @Override
    public LocalDate getConclusionDate() {
	return getPhdIndividualProgramProcess().getConclusionDate();
    }

    @Override
    public ExecutionYear getConclusionYear() {
	return getPhdIndividualProgramProcess().getConclusionYear();
    }

    @Override
    public String getGraduateTitle(Locale locale) {
	return getPhdIndividualProgramProcess().getGraduateTitle(locale);
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	try {
	    verifyIsToProcessAndHasPersonalInfo(academicServiceRequestBean);
	    verifyIsToDeliveredAndIsPayed(academicServiceRequestBean);
	} catch (DomainException e) {
	    throw new PhdDomainOperationException(e.getKey(), e, e.getArgs());
	}

	super.internalChangeState(academicServiceRequestBean);
	if (academicServiceRequestBean.isToProcess()) {
	    if (!getPhdIndividualProgramProcess().isConclusionProcessed()) {
		throw new PhdDomainOperationException("error.registryDiploma.registrationNotSubmitedToConclusionProcess");
	    }

	    if (isPayable() && !isPayed()) {
		throw new PhdDomainOperationException("AcademicServiceRequest.hasnt.been.payed");
	    }

	    if (getRegistryCode() == null) {

		PhdDiplomaRequest diplomaRequest = getPhdIndividualProgramProcess().getDiplomaRequest();

		if (diplomaRequest != null && diplomaRequest.hasRegistryCode()) {
		    diplomaRequest.getRegistryCode().addDocumentRequest(this);
		} else {
		    getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
		}

		getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
	    }

	    if (getLastGeneratedDocument() == null) {
		generateDocument();
	    }

	    getDiplomaSupplement().process();
	} else if (academicServiceRequestBean.isToConclude()) {
	    if (getDiplomaSupplement().isConcludedSituationAccepted()) {
		getDiplomaSupplement().conclude();
	    }
	} else if (academicServiceRequestBean.isToCancelOrReject()) {
	    if (hasEvent()) {
		getEvent().cancel(academicServiceRequestBean.getEmployee());
	    }

	    if (academicServiceRequestBean.isToCancel()) {
		getDiplomaSupplement().cancel(academicServiceRequestBean.getJustification());
	    }

	    if (academicServiceRequestBean.isToReject()) {
		getDiplomaSupplement().reject(academicServiceRequestBean.getJustification());
	    }
	}
    }

    @Override
    public String getDescription() {
	return getDescription(getAcademicServiceRequestType(), getDocumentRequestType().getQualifiedName() + "."
		+ DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA.name());
    }

    public static PhdRegistryDiplomaRequest create(final PhdDocumentRequestCreateBean bean) {
	return new PhdRegistryDiplomaRequest(bean);
    }

    @Override
    public byte[] generateDocument() {
	try {
	    final List<AdministrativeOfficeDocument> documents = (List<AdministrativeOfficeDocument>) AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator
		    .create(this);

	    String latexThesisTitle = getPhdIndividualProgramProcess().getLatexThesisTitle();

	    for (AdministrativeOfficeDocument administrativeOfficeDocument : documents) {
		administrativeOfficeDocument.addParameter("useLatex", !StringUtils.isEmpty(latexThesisTitle));
	    }

	    final AdministrativeOfficeDocument[] array = {};
	    byte[] data = ReportsUtils.exportMultipleToPdfAsByteArray(documents.toArray(array));

	    if (!StringUtils.isEmpty(latexThesisTitle)) {
		LatexStringRendererService latexService = new LatexStringRendererService();
		byte[] renderedThesisTitle = latexService.render(latexThesisTitle, LatexFontSize.NORMALSIZE);
		Integer xOffset = getHorizontalOffset() != null ? getHorizontalOffset() : 0;
		Integer yOffset = getVerticalOffset() != null ? getVerticalOffset() : 0;

		data = ReportsUtils.stampPdfAt(data, renderedThesisTitle, 0 + xOffset, -365 + yOffset);
	    }

	    DocumentRequestGeneratedDocument.store(this, documents.iterator().next().getReportFileName() + ".pdf", data);
	    return data;
	} catch (JRException e) {
	    e.printStackTrace();
	    throw new DomainException("error.phdDiplomaRequest.errorGeneratingDocument");
	} catch (LatexStringRendererException e) {
	    throw new DomainException("error.phdDiplomaRequest.latex.service", e);
	}
    }

    @Override
    public String getProgrammeTypeDescription() {
	return ResourceBundle.getBundle("resources.PhdResources", Locale.getDefault()).getString("label.php.program");
    }

    @Override
    public String getViewStudentProgrammeLink() {
	return "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId="
		+ getPhdIndividualProgramProcess().getExternalId();
    }

    @Override
    public String getReceivedActionLink() {
	return String
		.format("/phdAcademicServiceRequestManagement.do?method=prepareReceiveOnRectorate&amp;phdAcademicServiceRequestId=%s&amp;batchOid=%s",
			getExternalId(), getRectorateSubmissionBatch().getExternalId());
    }

}
